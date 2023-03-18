package com.yushun.recommender.security.utils;

import java.util.Random;

/**
 * <p>
 * Password Generater
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-10
 */

public class PasswordGenerator {
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_-+=?";

    public static String getRandomPassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(UPPER_CASE_LETTERS.length());
            sb.append(UPPER_CASE_LETTERS.charAt(index));
            index = random.nextInt(LOWER_CASE_LETTERS.length());
            sb.append(LOWER_CASE_LETTERS.charAt(index));
            index = random.nextInt(NUMBERS.length());
            sb.append(NUMBERS.charAt(index));
            index = random.nextInt(SPECIAL_CHARACTERS.length());
            sb.append(SPECIAL_CHARACTERS.charAt(index));
        }

        char[] chars = sb.toString().toCharArray();
        for(int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
}