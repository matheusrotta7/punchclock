package com.punchy.punchclock.service;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtils {

    public static void main(String[] args) {
        String passwordHash = DigestUtils.sha256Hex("felipesmith45");
        System.out.println(passwordHash);
    }
}
