package com.yushun.recommender.security.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Email Checker
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-10
 */

public class EmailChecker {
    private static String regEx = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    /**
     * The password must contain letters, numbers, and special characters, at least 8 characters, and a maximum of 16 characters
     * @param email
     * @return
     */
    public static boolean check(String email){
        Pattern Email_Pattern = Pattern.compile(regEx);
        Matcher matcher = Email_Pattern.matcher(email);

        if(matcher.matches()) {
            return true;
        }

        return false;
    }
}
