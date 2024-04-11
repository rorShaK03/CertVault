package ru.hse.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.hse.dto.requests.SignInRequest;
import ru.hse.dto.requests.SignUpRequest;
import ru.hse.dto.responses.JwtAuthenticationResponse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationControllerIntegrationTest {
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void createUserTest() {
        SignUpRequest record = new SignUpRequest("Kirill", "Krasnoslobodtsev", "2003kirillka@gmail.com", "1234567");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signup", record, JwtAuthenticationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void validSignInTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        SignInRequest signin = new SignInRequest("lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signup", signin, JwtAuthenticationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void invalidSignInTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        SignInRequest signin = new SignInRequest("lyrics.red@yandex.ru", "123457");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signin", signin,JwtAuthenticationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}
