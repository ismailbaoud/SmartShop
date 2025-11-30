package com.ismail.smartShop.helper;

import org.springframework.stereotype.Component;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Component
public class passwordHasher {

    public String hash(String raw) {
        return BCrypt.withDefaults().hashToString(12, raw.toCharArray());
    }

    public boolean check(String raw, String hashed) {
        return BCrypt.verifyer().verify(raw.toCharArray(), hashed).verified;
    }
}
