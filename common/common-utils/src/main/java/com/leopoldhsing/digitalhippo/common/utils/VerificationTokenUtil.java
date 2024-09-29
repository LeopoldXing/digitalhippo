package com.leopoldhsing.digitalhippo.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

public class VerificationTokenUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * generate a verification token to verify user's email
     *
     * @return
     */
    public static String generateVerificationToken() {
        String uuid = UUID.randomUUID().toString();

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String token = Base64Util.encode((uuid + new String(randomBytes, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));

        return token.replaceAll("[+/=]", "");
    }

    private VerificationTokenUtil() {
    }
}
