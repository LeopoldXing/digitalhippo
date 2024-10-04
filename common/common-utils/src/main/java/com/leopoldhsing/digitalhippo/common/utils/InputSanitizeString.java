package com.leopoldhsing.digitalhippo.common.utils;

import org.springframework.util.StringUtils;

public class InputSanitizeString {
    public static String sanitizeString(String str) {
        if (StringUtils.hasLength(str)) {
            return str.replaceAll("\0", "");
        } else return "";
    }

    private InputSanitizeString() {
    }
}
