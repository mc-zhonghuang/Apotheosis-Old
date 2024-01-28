package cn.hackedmc.alexander.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomUtil {
    private final static Random rand;

    public static int nextInt(int in, int out) {
        int max = Math.max(in, out), min = Math.min(in, out);
        return rand.nextInt(max - min + 1) + min;
    }

    public static String randomName() {
        return RandomStringUtils.random(nextInt(14, 8), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    static {
        rand = new Random();
    }
}
