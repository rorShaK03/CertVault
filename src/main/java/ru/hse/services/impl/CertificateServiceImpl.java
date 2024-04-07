package ru.hse.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.code.Decrypter;
import ru.hse.code.Encrypter;
import ru.hse.code.impl.DecrypterImpl;
import ru.hse.code.impl.EncrypterImpl;
import ru.hse.models.Right;
import ru.hse.models.Role;
import ru.hse.models.secrets.Certificate;
import ru.hse.models.secrets.Key;
import ru.hse.repositories.CertificateRepository;
import ru.hse.repositories.RightRepository;
import ru.hse.services.CertificateService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final RightRepository rightRepository;
    private final Encrypter encrypter;
    private final Decrypter decrypter;

    @Override
    public UUID addSecret(String secret, UUID userId) {
        Certificate cert = new Certificate(encrypter.encrypt(secret));
        certificateRepository.save(cert);
        Right right = new Right(userId, cert.getId(), Role.ADMIN);
        rightRepository.save(right);
        return cert.getVersionId();
    }

    @Override
    public Certificate findCertByVersionId(UUID versionId, UUID userId) throws IllegalArgumentException {
        Certificate cert = certificateRepository.getByVersionId(versionId);
        if (cert == null) {
            throw new IllegalArgumentException("Certificate with such version ID does not exist.");
        } else {
            Right right = rightRepository.findByUserIdAndSecretId(userId, cert.getId());
            if (right.getRole() == Role.USER) {
                throw new IllegalArgumentException("You have not enough rights");
            } else {
                cert.setSecret(decrypter.decrypt(cert.getSecret()));
                return cert;
            }
        }
    }

    @Override
    public void removeAllById(UUID secretId, UUID userId) {
        var certs = certificateRepository.getAllById(secretId);
        List<Certificate> toDelete = new ArrayList<>();
        for (var cert: certs) {
            Right right = rightRepository.findByUserIdAndSecretId(userId, cert.getId());
            if (right.getRole() == Role.EDITOR || right.getRole() == Role.ADMIN) {
                toDelete.add(cert);
            }
        }
        certificateRepository.deleteAll(toDelete);
    }

    @Override
    public List<Certificate> getAllCertsById(UUID secretId, UUID userId) {
        var certs = certificateRepository.getAllById(secretId);
        List<Certificate> result = new ArrayList<>();
        for (var cert: certs) {
            Right right = rightRepository.findByUserIdAndSecretId(userId, cert.getId());
            if (right.getRole() != Role.USER) {
                result.add(cert);
            }
        }
        return result;
    }

    @Override
    public UUID updateKey(UUID secretId, String secret, UUID userId) {
        Right right = rightRepository.findByUserIdAndSecretId(userId, secretId);
        if (right != null && (right.getRole() == Role.EDITOR || right.getRole() == Role.ADMIN)) {
            Certificate cert = new Certificate(encrypter.encrypt(secret));
            certificateRepository.save(cert);
            return cert.getVersionId();
        } else {
            throw new IllegalArgumentException("Have no enough rights for updating the certificate");
        }
    }
}
