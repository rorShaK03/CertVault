package ru.hse.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.code.Decrypter;
import ru.hse.code.Encrypter;
import ru.hse.code.impl.DecrypterImpl;
import ru.hse.code.impl.EncrypterImpl;
import ru.hse.models.secrets.Certificate;
import ru.hse.repositories.CertificateRepository;
import ru.hse.services.CertificateService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final Encrypter encrypter;
    private final Decrypter decrypter;

    @Override
    public UUID addSecret(String secret) {
        Certificate cert = new Certificate(encrypter.encrypt(secret));
        certificateRepository.save(cert);
        return cert.getVersionId();
    }

    @Override
    public Certificate findCertByVersionId(UUID versionId) throws IllegalArgumentException {
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
        var certs = getAllCertsById(secretId);
        certificateRepository.deleteAll(certs);
    }

    @Override
    public List<Certificate> getAllCertsById(UUID secretId) {
        return certificateRepository.getAllById(secretId);
    }
}
