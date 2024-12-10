package com.saintolivetree.stripe_events_listener_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    @Value("${encryption.key}")
    private String encryptionKey;

    public String encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedMessageBytes = cipher.doFinal(message.getBytes());

            return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedMessageBytes);
        } catch (Exception e) {
            throw new RuntimeException(e); //FIXME
        }
    }

    public String decrypt(String encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedMessageBytes = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedMessage));

            return new String(decryptedMessageBytes);
        } catch (Exception e) {
            throw new RuntimeException(e); //FIXME
        }
    }
}
