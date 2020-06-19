package me.sizzlemcgrizzle.superblocks.util;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SuperBlocksUtil {
    public static String getStringFromPotion(PotionEffect potion) {
        if (potion == null)
            return "none";
        
        PotionEffectType type = potion.getType();
        if (PotionEffectType.REGENERATION.equals(type))
            return "regen";
        else if (PotionEffectType.INCREASE_DAMAGE.equals(type))
            return "strength";
        else if (PotionEffectType.SPEED.equals(type))
            return "speed";
        else if (PotionEffectType.FAST_DIGGING.equals(type))
            return "haste";
        else if (PotionEffectType.JUMP.equals(type))
            return "jumpboost";
        else if (PotionEffectType.DAMAGE_RESISTANCE.equals(type))
            return "resistance";
        else if (PotionEffectType.FIRE_RESISTANCE.equals(type))
            return "fireres";
        else if (PotionEffectType.WATER_BREATHING.equals(type))
            return "waterbreath";
        else if (PotionEffectType.SLOW_DIGGING.equals(type))
            return "minefatigue";
        else if (PotionEffectType.SLOW.equals(type))
            return "slowness";
        else if (PotionEffectType.WEAKNESS.equals(type))
            return "weakness";
        
        return "none";
        
    }
    
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
}
