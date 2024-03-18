package ru.hse.services;

import org.springframework.stereotype.Service;
import ru.hse.models.secrets.Certificate;

import java.util.List;
import java.util.UUID;

public interface CertificateService {
    public UUID addSecret(String secret, UUID userId);
    public Certificate findCertByVersionId(UUID versionId, UUID userId);
    public void removeAllById(UUID secretId, UUID userId);
    public List<Certificate> getAllCertsById(UUID secretId, UUID userId);
}
