package com.leopoldhsing.digitalhippo.common.utils;

import java.util.UUID;

public class SignInTokenUtil {

    public static String generateAccessToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private SignInTokenUtil() {
    }
}
