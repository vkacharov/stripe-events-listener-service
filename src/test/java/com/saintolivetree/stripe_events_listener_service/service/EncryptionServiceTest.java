package com.saintolivetree.stripe_events_listener_service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class EncryptionServiceTest {
    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void testDecrypt() {
        String donorId = "CustomerId123";
        String encrypted = encryptionService.encrypt(donorId);
        String decrypted = encryptionService.decrypt(encrypted);
        Assertions.assertEquals(donorId, decrypted);
    }
}
