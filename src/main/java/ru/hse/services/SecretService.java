package ru.hse.services;

import org.springframework.stereotype.Service;
import ru.hse.models.secrets.Secret;
import ru.hse.models.secrets.types.Certificate;

import java.util.List;
import java.util.UUID;

@Service
public interface SecretService {
    public UUID addSecret(String secret);
    public Secret findSecretByVersionId(UUID versionId);
    public void removeAllById(UUID secretId);
    public List<? extends Secret> getAllSecretsById(UUID secretId);
}
