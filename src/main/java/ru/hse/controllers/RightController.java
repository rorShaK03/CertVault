package ru.hse.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.hse.dto.requests.CreateRightRequest;
import ru.hse.dto.responses.CreateRightResponse;
import ru.hse.errors.NotFoundError;
import ru.hse.models.User;
import ru.hse.services.RightService;

@Controller
@RequestMapping("/api/v1/right")
@RequiredArgsConstructor
public class RightController {
    private final RightService rightService;

    @PostMapping
    public ResponseEntity<CreateRightResponse> createRight(@AuthenticationPrincipal User user,
                                                           @RequestBody CreateRightRequest createRightRequest) {
        rightService.updateRight(user.getId(), createRightRequest.getSecretId(), createRightRequest.getUserId(),
                createRightRequest.getRole());
        return new ResponseEntity<CreateRightResponse>(new CreateRightResponse("Right was successfully created"), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<CreateRightResponse> updateRight(@AuthenticationPrincipal User user,
                                                           @RequestBody CreateRightRequest createRightRequest) {
        rightService.updateRight(user.getId(), createRightRequest.getSecretId(), createRightRequest.getUserId(),
                createRightRequest.getRole());
        return new ResponseEntity<CreateRightResponse>(new CreateRightResponse("Right was successfully updated"), HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<NotFoundError> badRequestHandle(IllegalArgumentException e) {
        return new ResponseEntity<>(new NotFoundError(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
