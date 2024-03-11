package ru.hse.services.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.hse.models.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hse.dto.requests.SignInRequest;
import ru.hse.dto.requests.SignUpRequest;
import ru.hse.dto.responses.JwtAuthenticationResponse;
import ru.hse.models.Role;
import ru.hse.repositories.UserRepository;
import ru.hse.services.AuthenticationService;
import ru.hse.services.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = new User(request.getFirstName(),
                            request.getLastName(),
                            request.getLogin(),
                            passwordEncoder.encode(request.getPassword()),
                            Role.USER);
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        var user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
