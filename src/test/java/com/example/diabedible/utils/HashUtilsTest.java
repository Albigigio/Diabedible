package com.example.diabedible.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashUtilsTest {

    @Test
    void createAndVerifyToken_success() {
        String pwd = "s3cr3t!";
        String token = HashUtils.createPasswordToken(pwd);
        Assertions.assertTrue(token.startsWith("pbkdf2$"));
        Assertions.assertTrue(HashUtils.verifyPassword(pwd, token));
        Assertions.assertFalse(HashUtils.verifyPassword("wrong", token));
        Assertions.assertFalse(HashUtils.needsUpgrade(token));
    }

    @Test
    void legacyHex_needsUpgradeAndVerifies() {
        String legacy = HashUtils.sha256Hex("abc");
        String token = "legacy-sha256$" + legacy;
        Assertions.assertTrue(HashUtils.verifyPassword("abc", token));
        Assertions.assertTrue(HashUtils.needsUpgrade(token));
    }

    @Test
    void bareHexTreatedAsLegacy() {
        String legacy = HashUtils.sha256Hex("xyz");
        Assertions.assertTrue(HashUtils.verifyPassword("xyz", legacy));
        Assertions.assertTrue(HashUtils.needsUpgrade(legacy));
    }
}
