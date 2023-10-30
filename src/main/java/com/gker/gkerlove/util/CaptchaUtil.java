package com.gker.gkerlove.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {
    public static BufferedImage generateImage(String verifyCode) {
        int width = 80;
        int height = 32;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("Arial", Font.BOLD, 20));
        for (int i = 0; i < verifyCode.length(); i++) {
            String s = String.valueOf(verifyCode.charAt(i));
            graphics2D.drawString(s, 10 + i * 16, 22);
        }
        for (int i = 0; i < 10; i++) {
            graphics2D.setColor(new Color(150 + new Random().nextInt(100), 150 + new Random().nextInt(100), 150 + new Random().nextInt(100)));
            graphics2D.drawLine(new Random().nextInt(width), new Random().nextInt(height), new Random().nextInt(width), new Random().nextInt(height));
        }
        graphics2D.dispose();
        return image;
    }
}