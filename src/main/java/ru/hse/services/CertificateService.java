package ru.hse.services;

import org.springframework.stereotype.Service;
import ru.hse.models.secrets.Certificate;

import java.util.List;
import java.util.UUID;

public interface CertificateService {
    public UUID addSecret(String secret);
    public Certificate findCertByVersionId(UUID versionId);
    public void removeAllById(UUID secretId);
    public List<Certificate> getAllCertsById(UUID secretId);
}
