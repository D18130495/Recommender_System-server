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
    public static void main(String[] args) {
        String randomPassword = getRandomPassword(16);
        System.out.println(randomPassword);
    }

    public static String getRandomPassword(int len){
        String str = null;
        char charr[] = "abcdefghjkmnprstuvwxyzABCDEFGHJKMNPQRST2345678!@#$%^&*-+=:;<>,?/".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for(int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }

        str = sb.toString();

        String pattern1 = ".*[a-z]+.*";
        String pattern2 = ".*[A-Z]+.*";
        String pattern3 = ".*[0-9]+.*";
        String pattern4 = ".*[~!@#$%^&*+=:;<>,?/]+.*";

        if(!(str.matches(pattern1) && str.matches(pattern2) && str.matches(pattern3) && str.matches(pattern4))) {
            str = getRandomPassword(len);
        }

        return str;
    }
}
