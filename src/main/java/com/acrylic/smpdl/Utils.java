package com.acrylic.smpdl;

import org.bukkit.ChatColor;

public final class Utils {

    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String[] colorize(String[] arr) {
        String[] newArr = new String[arr.length];
        int i = 0;
        for (String str : arr) {
            newArr[i] = colorize(str);
            i++;
        }
        return newArr;
    }

    public static double round2dp(double num) {
        return Math.round(num * 100) / 100f;
    }

}
