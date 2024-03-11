package ru.hse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.models.secrets.Key;

import java.util.List;
import java.util.UUID;

@Repository
public interface KeyRepository extends JpaRepository<Key, UUID> {
    public Key getByVersionId(UUID versionId);
    public List<Key> getAllById(UUID secretId);
}
