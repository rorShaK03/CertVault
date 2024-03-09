package ru.hse.code.impl;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hse.code.Decrypter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class DecrypterImpl implements Decrypter {
    @Value("${encryption.key}")
    private String key;

    @Value("${encryption.initVector}")
    private String initVector;

    @Value("${encryption.charset}")
    private String charset;

    @Value("${encryption.algorithm}")
    private String algorithm;

    @Value("${encryption.transformation}")
    private String transformation;

    public String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(charset));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(charset), algorithm);

            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
