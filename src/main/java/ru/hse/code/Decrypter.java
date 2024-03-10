package ru.hse.code;

import org.springframework.stereotype.Service;

@Service
public interface Decrypter {
    public String decrypt(String encrypted);
}
