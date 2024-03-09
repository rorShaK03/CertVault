package ru.hse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.controller.response.SecretVersionsResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {

    @PostMapping
    public ResponseEntity<UUID> uploadNewCert(String body) {
        // Returns version_id
        return ResponseEntity.status(HttpStatus.CREATED).body(UUID.randomUUID());
    }

    // Получить секрет можно только по version_id
    @GetMapping("/{versionId}")
    public ResponseEntity<UUID> getCert(@PathVariable UUID versionId) {
        return ResponseEntity.ok(UUID.randomUUID());
    }

    // Можно получить список version_id всех версий по secret_id
    @GetMapping("/versions/{secretId}")
    public ResponseEntity<SecretVersionsResponse> getCertVersions(@PathVariable UUID secretId) {
        return ResponseEntity.ok(new SecretVersionsResponse(List.of()));
    }



}
