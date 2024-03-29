package com.gker.gkerlove.util;

import java.text.DecimalFormat;
import java.util.Random;

public class RandomUtil {

    private static final Random random = new Random();

    private static final DecimalFormat fourBits = new DecimalFormat("0000");

    private static final DecimalFormat sixBits = new DecimalFormat("000000");

    public static String getFourBitRandom() {
        return fourBits.format(random.nextInt(10000));
    }

    public static String getSixBitRandom() {
        return sixBits.format(random.nextInt(1000000));
    }

}