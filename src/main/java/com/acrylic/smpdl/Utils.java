package com.acrylic.smpdl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

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
        return ((int) (num * 100)) / 100f;
    }

    public static LivingEntity getAttackerFrom(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Arrow) {
            ProjectileSource shooter = ((Arrow) damager).getShooter();
            if (shooter instanceof LivingEntity)
                return (LivingEntity) shooter;
        } else if (damager instanceof LivingEntity) {
            return (LivingEntity) damager;
        }
        return null;
    }

}
