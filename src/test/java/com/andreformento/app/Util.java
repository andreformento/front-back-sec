package com.andreformento.app;

import java.util.Random;

public class Util {

    private final static Random RANDOM = new Random();

    public static String createAnEmail() {
        return "eddie" + RANDOM.nextInt(Integer.MAX_VALUE) + "@test.com";
    }

}
