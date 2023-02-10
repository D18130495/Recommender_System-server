package com.yushun.recommender.security.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Password Checker
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-10
 */

public class PasswordChecker {
    private static String regEx = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(.{8,16})$";

    /**
     * The password must contain letters, numbers, and special characters, at least 8 characters, and a maximum of 16 characters
     * @param password
     * @return
     */
    public static boolean check(String password){
        Pattern Password_Pattern = Pattern.compile(regEx);
        Matcher matcher = Password_Pattern.matcher(password);

        if(matcher.matches()) {
            return true;
        }

        return false;
    }
}
