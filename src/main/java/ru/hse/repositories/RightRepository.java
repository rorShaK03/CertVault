package ru.hse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.models.Right;

import java.util.UUID;

public interface RightRepository extends JpaRepository<Right, UUID> {
    public Right findByUserIdAndSecretId(UUID userId, UUID secretId);
}
