package ru.hse.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.code.Decrypter;
import ru.hse.code.Encrypter;
import ru.hse.models.Right;
import ru.hse.models.Role;
import ru.hse.models.secrets.Key;
import ru.hse.repositories.KeyRepository;
import ru.hse.repositories.RightRepository;
import ru.hse.services.KeyService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {
    private final KeyRepository keyRepository;
    private final RightRepository rightRepository;
    private final Encrypter encrypter;
    private final Decrypter decrypter;

    @Override
    public UUID addSecret(String secret, UUID userId) {
        Key key = new Key(encrypter.encrypt(secret));
        keyRepository.save(key);
        Right right = new Right(userId, key.getId(), Role.ADMIN);
        rightRepository.save(right);
        return key.getVersionId();
    }

    @Override
    public Key findKeyByVersionId(UUID versionId, UUID userId) throws IllegalArgumentException {
        Key key = keyRepository.getByVersionId(versionId);
        if (key == null) {
            throw new IllegalArgumentException("Certificate with such version ID does not exist.");
        } else {
            Right right = rightRepository.findByUserIdAndSecretId(userId, key.getId());
            if (right.getRole() == Role.USER) {
                throw new IllegalArgumentException("You have not enough rights");
            } else {
                key.setSecret(decrypter.decrypt(key.getSecret()));
                return key;
            }
        }
    }

    @Override
    public void removeAllById(UUID secretId, UUID userId) {
        var keys = keyRepository.getAllById(secretId);
        List<Key> toDelete = new ArrayList<>();
        for (var key: keys) {
            Right right = rightRepository.findByUserIdAndSecretId(userId, key.getId());
            if (right.getRole() == Role.EDITOR || right.getRole() == Role.ADMIN) {
                toDelete.add(key);
            }
        }
        keyRepository.deleteAll(toDelete);
    }

    @Override
    public List<Key> getAllKeysById(UUID secretId, UUID userId) {
        var keys = keyRepository.getAllById(secretId);
        List<Key> result = new ArrayList<>();
        for (var key: keys) {
            Right right = rightRepository.findByUserIdAndSecretId(userId, key.getId());
            if (right.getRole() != Role.USER) {
                result.add(key);
            }
        }
        return result;
    }

    @Override
    public UUID updateKey(UUID secretId, String secret, UUID userId) {
        Right right = rightRepository.findByUserIdAndSecretId(userId, secretId);
        if (right != null && (right.getRole() == Role.EDITOR || right.getRole() == Role.ADMIN)) {
            Key key = new Key(encrypter.encrypt(secret));
            key.setId(secretId);
            keyRepository.save(key);
            return key.getVersionId();
        } else {
            throw new IllegalArgumentException("Have no enough rights for updating the key");
        }
    }
}
