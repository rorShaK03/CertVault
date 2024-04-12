package ru.hse.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.hse.dto.requests.UploadSecretRequest;
import ru.hse.dto.responses.SecretVersionsResponse;
import ru.hse.errors.NotFoundError;
import ru.hse.models.User;
import ru.hse.models.secrets.Certificate;
import ru.hse.services.impl.CertificateServiceImpl;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/certificate")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateServiceImpl certificateService;

    @PostMapping
    public ResponseEntity<UUID> uploadNewCert(@AuthenticationPrincipal User user, @RequestBody UploadSecretRequest body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificateService.addSecret(body.getSecret(), user.getId()));
    }

    // Удаляются сразу все версии секрета по secretId
    @PostMapping("/remove/{secretId}")
    public ResponseEntity<UUID> removeCert(@AuthenticationPrincipal User user, @PathVariable("secretId") UUID secretId) {
        certificateService.removeAllById(secretId, user.getId());
        return ResponseEntity.ok(secretId);
    }

    @PostMapping("/update/{secretId}")
    public ResponseEntity<UUID> updateCert(@AuthenticationPrincipal User user, @PathVariable("secretId") UUID secretId,
                                           @RequestBody UploadSecretRequest body) {
        // Returns version_id
        return new ResponseEntity<UUID>(certificateService.updateKey(secretId, body.getSecret(), user.getId()), HttpStatus.OK);
    }

    // Получить секрет можно только по version_id
    @GetMapping("/{versionId}")
    public ResponseEntity<Certificate> getCert(@AuthenticationPrincipal User user, @PathVariable("versionId") UUID versionId) {
        Certificate cert = certificateService.findCertByVersionId(versionId, user.getId());
        return new ResponseEntity<>(cert, HttpStatus.OK);
    }

    // Можно получить список version_id всех версий по secret_id
    @GetMapping("/versions/{secretId}")
    public ResponseEntity<SecretVersionsResponse> getCertVersions(@AuthenticationPrincipal User user,
                                                                  @PathVariable("secretId") UUID secretId) {
        return ResponseEntity.ok(new SecretVersionsResponse(certificateService.getAllCertsById(secretId, user.getId())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<NotFoundError> badRequestHandle(IllegalArgumentException e) {
        return new ResponseEntity<>(new NotFoundError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

}