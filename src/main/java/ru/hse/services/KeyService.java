package ru.hse.services;

import org.springframework.stereotype.Service;
import ru.hse.models.secrets.Key;

import java.util.List;
import java.util.UUID;

public interface KeyService {
    public UUID addSecret(String secret);
    public Key findKeyByVersionId(UUID versionId);
    public void removeAllById(UUID secretId);
    public List<Key> getAllKeysById(UUID secretId);
}
