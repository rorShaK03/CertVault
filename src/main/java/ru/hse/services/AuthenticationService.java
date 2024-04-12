package ru.hse.services;

import ru.hse.dto.requests.SignInRequest;
import ru.hse.dto.requests.SignUpRequest;
import ru.hse.dto.responses.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);
    JwtAuthenticationResponse signIn(SignInRequest request);
}
