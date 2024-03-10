package ru.hse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.models.secrets.types.Certificate;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    public Certificate getByVersionId(UUID versionId);
    public void deleteById(UUID id);
    public List<Certificate> getAllById(UUID secretId);
}
