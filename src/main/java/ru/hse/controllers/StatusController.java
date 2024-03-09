package ru.hse.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.dao.responses.StatusResponse;

@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class StatusController {
    public ResponseEntity<StatusResponse> getStatus() {
        return ResponseEntity.ok(new StatusResponse("Server is ready."));
    }
}
