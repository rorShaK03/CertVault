package ru.hse.code;

import org.springframework.stereotype.Service;

@Service
public interface Encrypter {
    public String encrypt(String secret);
}
