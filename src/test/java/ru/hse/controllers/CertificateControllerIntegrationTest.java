package ru.hse.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import ru.hse.dto.requests.SignInRequest;
import ru.hse.dto.requests.SignUpRequest;
import ru.hse.dto.requests.UploadSecretRequest;
import ru.hse.dto.responses.JwtAuthenticationResponse;
import ru.hse.dto.responses.SecretVersionsResponse;
import ru.hse.models.secrets.Certificate;
import ru.hse.repositories.CertificateRepository;
import ru.hse.repositories.RightRepository;
import ru.hse.repositories.UserRepository;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CertificateControllerIntegrationTest {
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private RightRepository rightRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createKeyTest() {
        SignInRequest record = new SignInRequest("2003kirillka@gmail.com", "1234567");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signin", record, JwtAuthenticationResponse.class);
        UploadSecretRequest cert = new UploadSecretRequest("secret");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + response.getBody().getToken());
        ResponseEntity<UUID> postResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate", HttpMethod.POST, new HttpEntity<>(cert, headers), UUID.class);
        ResponseEntity<Certificate> getResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/" + postResponse.getBody().toString(), HttpMethod.GET, new HttpEntity<>(headers), Certificate.class);
        assertThat(postResponse.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(getResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse.getBody().getSecret(), is("secret"));
    }

    @Test
    public void deleteKeyTest() {
        SignInRequest record = new SignInRequest("2003kirillka@gmail.com", "1234567");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signin", record, JwtAuthenticationResponse.class);
        UploadSecretRequest cert = new UploadSecretRequest("secret");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + response.getBody().getToken());
        ResponseEntity<UUID> postResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate", HttpMethod.POST, new HttpEntity<>(cert, headers), UUID.class);
        ResponseEntity<Certificate> getResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/" + postResponse.getBody().toString(), HttpMethod.GET, new HttpEntity<>(headers), Certificate.class);
        ResponseEntity<UUID> deleteResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/remove/" + getResponse.getBody().getId(), HttpMethod.POST, new HttpEntity<>(headers), UUID.class);
        ResponseEntity<Certificate> getDeletedResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/" + postResponse.getBody().toString(), HttpMethod.GET, new HttpEntity<>(headers), Certificate.class);
        assertThat(getDeletedResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void updateKeyTest() {
        SignInRequest record = new SignInRequest("2003kirillka@gmail.com", "1234567");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signin", record, JwtAuthenticationResponse.class);
        UploadSecretRequest cert1 = new UploadSecretRequest("secret1");
        UploadSecretRequest cert2 = new UploadSecretRequest("secret2");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + response.getBody().getToken());
        ResponseEntity<UUID> postResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate", HttpMethod.POST, new HttpEntity<>(cert1, headers), UUID.class);
        ResponseEntity<Certificate> getResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/" + postResponse.getBody().toString(), HttpMethod.GET, new HttpEntity<>(headers), Certificate.class);
        ResponseEntity<UUID> deleteResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/update/" + getResponse.getBody().getId(), HttpMethod.POST, new HttpEntity<>(cert2, headers), UUID.class);
        ResponseEntity<SecretVersionsResponse> getVersionsResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/versions/" + getResponse.getBody().getId().toString(), HttpMethod.GET, new HttpEntity<>(headers), SecretVersionsResponse.class);
        assertThat(getVersionsResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getVersionsResponse.getBody().getVersions().size(), is(2));
    }

    @Test
    public void lackOfRightsTest() {
        SignUpRequest another = new SignUpRequest("Lyrics", "Blue", "lyrics.blue@yandex.ru", "111111");
        ResponseEntity<JwtAuthenticationResponse> anotherResponse = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signup", another, JwtAuthenticationResponse.class);
        SignInRequest record = new SignInRequest("2003kirillka@gmail.com", "1234567");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("http://localhost:8080/api/v1/auth/signin", record, JwtAuthenticationResponse.class);
        UploadSecretRequest key1 = new UploadSecretRequest("secret1");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + response.getBody().getToken());
        HttpHeaders anotherHeaders = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + anotherResponse.getBody().getToken());
        ResponseEntity<UUID> postResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate", HttpMethod.POST, new HttpEntity<>(key1, headers), UUID.class);
        ResponseEntity<Certificate> getResponse = restTemplate.exchange("http://localhost:8080/api/v1/certificate/" + postResponse.getBody().toString(), HttpMethod.GET, new HttpEntity<>(anotherHeaders), Certificate.class);
        assertThat(getResponse.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}
