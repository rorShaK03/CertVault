package ru.hse.dao.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.models.secrets.Secret;
import ru.hse.models.secrets.types.Certificate;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class SecretVersionsResponse {
    class SecretVersion {
        int versionNum;
        UUID version_id;
        public SecretVersion(int versionNum, UUID version_id) {
            this.version_id = version_id;
            this.versionNum = versionNum;
        }
    }

    List<SecretVersion> versions;

    public SecretVersionsResponse(List<? extends Secret> certs) {
        for (var cert: certs) {
            this.versions.add(new SecretVersion(versions.size() + 1, cert.getVersionId()));
        }
    }
}
