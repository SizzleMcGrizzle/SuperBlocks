package me.sizzlemcgrizzle.superblocks.util;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SuperBlocksUtil {
    
    public static PotionEffect getPotionFromString(String string) {
        switch (string) {
            case "regen":
                return new PotionEffect(PotionEffectType.REGENERATION, 180, 0);
            case "strength":
                return new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 180, 1);
            case "speed":
                return new PotionEffect(PotionEffectType.SPEED, 180, 1);
            case "haste":
                return new PotionEffect(PotionEffectType.FAST_DIGGING, 180, 1);
            case "resistance":
                return new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 180, 1);
            case "jumpboost":
                return new PotionEffect(PotionEffectType.JUMP, 180, 1);
            case "fireres":
                return new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 180, 0);
            case "waterbreath":
                return new PotionEffect(PotionEffectType.WATER_BREATHING, 180, 0);
            case "minefatigue":
                return new PotionEffect(PotionEffectType.SLOW_DIGGING, 180, 0);
            case "slowness":
                return new PotionEffect(PotionEffectType.SLOW, 180, 0);
            case "weakness":
                return new PotionEffect(PotionEffectType.WEAKNESS, 180, 0);
            default:
                return null;
        }
    }
    
    public static PotionEffect getPotionFromType(PotionEffectType type) {
        switch (type.getName()) {
            case "INCREASE_DAMAGE":
            case "SPEED":
            case "FAST_DIGGING":
            case "JUMP":
                return new PotionEffect(type, 180, 1);
            default:
                return new PotionEffect(type, 180, 0);
        }
    }
}
