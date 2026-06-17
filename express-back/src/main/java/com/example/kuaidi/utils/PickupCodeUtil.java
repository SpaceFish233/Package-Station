package com.example.kuaidi.utils;

import java.util.Random;

public class PickupCodeUtil {
    private static final Random RANDOM = new Random();

    public static String generate() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }
}
