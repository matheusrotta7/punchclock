package com.punchy.punchclock.utils;


import org.springframework.stereotype.Component;

public class StringUtils {

    public static String nullSafeToString(Object obj) {
        return obj == null ? null : obj.toString();
    }
}
