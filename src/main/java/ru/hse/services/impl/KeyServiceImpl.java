package ru.hse.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.code.Decrypter;
import ru.hse.code.Encrypter;
import ru.hse.models.secrets.Key;
import ru.hse.repositories.KeyRepository;
import ru.hse.services.KeyService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {
    private final KeyRepository keyRepository;
    private final Encrypter encrypter;
    private final Decrypter decrypter;

    @Override
    public UUID addSecret(String secret) {
        Key key = new Key(encrypter.encrypt(secret));
        keyRepository.save(key);
        return key.getVersionId();
    }

    @Override
    public Key findKeyByVersionId(UUID versionId) throws IllegalArgumentException {
        Key key = keyRepository.getByVersionId(versionId);
        if (key == null) {
            throw new IllegalArgumentException("Certificate with such version ID does not exist.");
        } else {
            key.setSecret(decrypter.decrypt(key.getSecret()));
            return key;
        }
    }

    @Override
    public void removeAllById(UUID secretId) {
        var certs = getAllKeysById(secretId);
        keyRepository.deleteAll(certs);
    }

    @Override
    public List<Key> getAllKeysById(UUID secretId) {
        return keyRepository.getAllById(secretId);
    }
}
