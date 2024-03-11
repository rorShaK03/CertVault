package ru.hse.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hse.dto.responses.StatusResponse;

@Controller
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class StatusController {
    @GetMapping
    public ResponseEntity<StatusResponse> getStatus() {
        return ResponseEntity.ok(new StatusResponse("Server is ready."));
    }
}
