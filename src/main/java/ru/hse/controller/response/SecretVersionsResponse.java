package ru.hse.controller.response;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class SecretVersionsResponse {
    class SecretVersion {
        int versionNum;
        UUID version_id;
    }
    List<SecretVersion> versions;
}
