package ru.hse.dto.responses;

import lombok.*;
import ru.hse.models.secrets.Versionable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class SecretVersionsResponse {
    List<SecretVersion> versions;

    @Getter
    @NoArgsConstructor
    public static class SecretVersion {
        Integer versionNum;
        UUID version_id;
        public SecretVersion(int versionNum, UUID version_id) {
            this.version_id = version_id;
            this.versionNum = versionNum;
        }
    }

    public SecretVersionsResponse(List<? extends Versionable> certs) {
        versions = new ArrayList<>();
        for (var cert: certs) {
            this.versions.add(new SecretVersion(versions.size() + 1, cert.getVersionId()));
        }
    }
}
