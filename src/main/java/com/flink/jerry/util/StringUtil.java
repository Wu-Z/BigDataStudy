package com.flink.jerry.util;

import java.util.Random;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/5/31 10:13
 */
public class StringUtil {

    private static final char[] BASESTRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String getRandomString(int length){

        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        while (length-->0) {randomString.append(BASESTRING[random.nextInt(52)]);}

        return randomString.toString();
    }

}
