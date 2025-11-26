package com.ismail.smartShop.helper;

import java.security.SecureRandom;

public class CodeGenerater {
    public static final String PREFIX = "PROMO-";
    public static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final SecureRandom RANDOM = new SecureRandom();

    public String generatePromoCode() {
        StringBuilder sb = new StringBuilder(PREFIX);
        for(int i = 0; i<4; i++) {
            Integer index = RANDOM.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(index));
        }
        return sb.toString();
    }
}
