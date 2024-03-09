package ru.hse.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.dao.requests.UploadSecretRequest;
import ru.hse.dao.responses.SecretVersionsResponse;
import ru.hse.errors.NotFoundError;
import ru.hse.models.secrets.types.Certificate;
import ru.hse.services.impl.CertificateService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/certificate")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;

    @PostMapping
    public ResponseEntity<UUID> uploadNewCert(UploadSecretRequest body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificateService.addSecret(body.getSecret()));
    }

    // Удаляются сразу все версии секрета по secretId
    @PostMapping("/remove/{secretId}")
    public ResponseEntity<UUID> removeCert(@PathVariable UUID secretId) {
        certificateService.removeAllById(secretId);
        return ResponseEntity.ok(secretId);
    }

    // Получить секрет можно только по version_id
    @GetMapping("/{versionId}")
    public ResponseEntity<Certificate> getCert(@PathVariable UUID versionId) {
        return ResponseEntity.ok(certificateService.findSecretByVersionId(versionId));
    }

    // Можно получить список version_id всех версий по secret_id
    @GetMapping("/versions/{secretId}")
    public ResponseEntity<SecretVersionsResponse> getCertVersions(@PathVariable UUID secretId) {
        return ResponseEntity.ok(new SecretVersionsResponse(certificateService.getAllSecretsById(secretId)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<NotFoundError> badRequestHandle(IllegalArgumentException e) {
        return new ResponseEntity<>(new NotFoundError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
