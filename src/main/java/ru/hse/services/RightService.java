package ru.hse.services;

import ru.hse.models.Role;
import ru.hse.models.secrets.Key;

import java.util.List;
import java.util.UUID;

public interface RightService {
    public void updateRight(UUID curUserId, UUID secretId, UUID userId, Role role);
}
