package ru.hse.services;

import org.springframework.stereotype.Service;
import ru.hse.models.secrets.Key;

import java.util.List;
import java.util.UUID;

public interface KeyService {
    public UUID addSecret(String secret, UUID userId);
    public Key findKeyByVersionId(UUID versionId, UUID userId);
    public void removeAllById(UUID secretId, UUID userId);
    public List<Key> getAllKeysById(UUID secretId, UUID userId);
}