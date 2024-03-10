package ru.hse.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.code.impl.DecrypterImpl;
import ru.hse.code.impl.EncrypterImpl;
import ru.hse.models.secrets.Secret;
import ru.hse.models.secrets.types.Certificate;
import ru.hse.repositories.CertificateRepository;
import ru.hse.services.SecretService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateService implements SecretService {
    private final CertificateRepository certificateRepository;
    private final EncrypterImpl encrypter;
    private final DecrypterImpl decrypter;

    @Override
    public UUID addSecret(String secret) {
        Certificate cert = new Certificate(encrypter.encrypt(secret));
        certificateRepository.save(cert);
        return cert.getVersionId();
    }

    @Override
    public Certificate findSecretByVersionId(UUID versionId) throws IllegalArgumentException {
        Certificate cert = certificateRepository.getByVersionId(versionId);
        if (cert == null) {
            throw new IllegalArgumentException("Certificate with such version ID does not exist.");
        } else {
            cert.setSecret(decrypter.decrypt(cert.getSecret()));
            return cert;
        }
    }

    @Override
    public void removeAllById(UUID secretId) {
        certificateRepository.deleteById(secretId);
    }

    @Override
    public List<? extends Secret> getAllSecretsById(UUID secretId) {
        return certificateRepository.getAllById(secretId);
    }
}
