package ru.hse.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.hse.dto.requests.UploadSecretRequest;
import ru.hse.dto.responses.SecretVersionsResponse;
import ru.hse.errors.NotFoundError;
import ru.hse.models.secrets.Key;
import ru.hse.services.KeyService;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/key")
@RequiredArgsConstructor
public class KeyController {
    private final KeyService keyService;
    @PostMapping
    public ResponseEntity<UUID> uploadNewKey(@RequestBody UploadSecretRequest body) {
        // Returns version_id
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(keyService.addSecret(body.getSecret()));
    }

    // Получить секрет можно только по version_id
    @GetMapping("/{versionId}")
    public ResponseEntity<Key> getKey(@PathVariable("versionId") UUID versionId) {
        return ResponseEntity.ok(keyService.findKeyByVersionId(versionId));
    }

    // Удаляются сразу все версии секрета по secretId
    @PostMapping("/remove/{secretId}")
    public ResponseEntity<UUID> removeCert(@PathVariable("secretId") UUID secretId) {
        // Returns secret_id
        keyService.removeAllById(secretId);
        return ResponseEntity.ok(secretId);
    }

    // Можно получить список version_id всех версий по secret_id
    @GetMapping("/versions/{secretId}")
    public ResponseEntity<SecretVersionsResponse> getKeyVersions(@PathVariable("secretId") UUID secretId) {
        return ResponseEntity.ok(new SecretVersionsResponse(keyService.getAllKeysById(secretId)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<NotFoundError> badRequestHandle(IllegalArgumentException e) {
        return new ResponseEntity<>(new NotFoundError(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
