package me.sizzlemcgrizzle.superblocks;

import de.craftlancer.core.menu.ConditionalMenu;
import de.craftlancer.core.menu.MenuClick;
import de.craftlancer.core.menu.MenuItem;
import de.craftlancer.core.resourcepack.TranslateSpaceFont;
import de.craftlancer.core.util.ItemBuilder;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import de.craftlancer.core.util.Tuple;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class SuperBeaconMenu extends ConditionalMenu {
    
    private SuperBeacon beacon;
    
    public SuperBeaconMenu(SuperBeacon beacon) {
        super(SuperBlocksPlugin.instance, 4, Arrays.asList(new Tuple<>("default", ""), new Tuple<>("resource", TranslateSpaceFont.TRANSLATE_NEGATIVE_8 + "§f\uE301")));
        
        this.beacon = beacon;
        getMenu("default").fill(new MenuItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("").build()), false);
        
        setItems();
    }
    
    private void setItems() {
        PotionItem buffRegenerationItem = new PotionItem(new ItemBuilder(Material.POTION).setDisplayName("&d&lRegeneration I")
                .setLore(containsEffect(PotionEffectType.REGENERATION) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Regeneration I")
                .setEnchantmentGlow(containsEffect(PotionEffectType.REGENERATION)).build(),
                PotionEffectType.REGENERATION);
        
        PotionItem buffStrengthItem = new PotionItem(new ItemBuilder(Material.BLAZE_POWDER).setDisplayName("&c&lStrength II")
                .setLore(containsEffect(PotionEffectType.INCREASE_DAMAGE) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Strength II")
                .setEnchantmentGlow(containsEffect(PotionEffectType.INCREASE_DAMAGE)).build(),
                PotionEffectType.INCREASE_DAMAGE);
        
        PotionItem buffSpeedItem = new PotionItem(new ItemBuilder(Material.SUGAR).setDisplayName("&f&lSpeed II")
                .setLore((containsEffect(PotionEffectType.SPEED)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Speed II")
                .setEnchantmentGlow(containsEffect(PotionEffectType.SPEED)).build(),
                PotionEffectType.SPEED);
        
        PotionItem buffHasteItem = new PotionItem(new ItemBuilder(Material.GOLDEN_PICKAXE).setDisplayName("&6&lHaste II")
                .setLore((containsEffect(PotionEffectType.FAST_DIGGING)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Haste II")
                .setEnchantmentGlow(containsEffect(PotionEffectType.FAST_DIGGING)).build(),
                PotionEffectType.FAST_DIGGING);
        
        PotionItem buffResistanceItem = new PotionItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("&9&lResistance II")
                .setLore((containsEffect(PotionEffectType.DAMAGE_RESISTANCE)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Resistance II")
                .setEnchantmentGlow(containsEffect(PotionEffectType.DAMAGE_RESISTANCE)).build(),
                PotionEffectType.DAMAGE_RESISTANCE);
        
        PotionItem buffJumpBoostItem = new PotionItem(new ItemBuilder(Material.FEATHER).setDisplayName("&a&lJump Boost II")
                .setLore((containsEffect(PotionEffectType.JUMP)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Jump Boost II")
                .setEnchantmentGlow(containsEffect(PotionEffectType.JUMP)).build(),
                PotionEffectType.JUMP);
        
        PotionItem buffWaterBreathingItem = new PotionItem(new ItemBuilder(Material.WATER_BUCKET).setDisplayName("&b&lWater Breathing")
                .setLore((containsEffect(PotionEffectType.WATER_BREATHING)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Water Breathing")
                .setEnchantmentGlow(containsEffect(PotionEffectType.WATER_BREATHING)).build(),
                PotionEffectType.WATER_BREATHING);
        
        PotionItem buffFireResistanceItem = new PotionItem(new ItemBuilder(Material.LAVA_BUCKET).setDisplayName("&6&lFire Resistance")
                .setLore((containsEffect(PotionEffectType.FIRE_RESISTANCE)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Fire Resistance")
                .setEnchantmentGlow(containsEffect(PotionEffectType.FIRE_RESISTANCE)).build(),
                PotionEffectType.FIRE_RESISTANCE);
        
        PotionItem debuffSlownessItem = new PotionItem(new ItemBuilder(Material.SOUL_SAND).setDisplayName("&8&lSlowness I")
                .setLore((containsEffect(PotionEffectType.SLOW)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Slowness I")
                .setEnchantmentGlow(containsEffect(PotionEffectType.SLOW)).build(),
                PotionEffectType.SLOW);
        
        PotionItem debuffWeaknessItem = new PotionItem(new ItemBuilder(Material.FIREWORK_STAR).setDisplayName("&7&lWeakness I")
                .setLore((containsEffect(PotionEffectType.WEAKNESS)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Weakness I")
                .setEnchantmentGlow(containsEffect(PotionEffectType.WEAKNESS)).build(),
                PotionEffectType.WEAKNESS);
        
        PotionItem debuffMiningFatigueItem = new PotionItem(new ItemBuilder(Material.WOODEN_PICKAXE).setDisplayName("&e&lMining Fatigue I")
                .setLore((containsEffect(PotionEffectType.SLOW_DIGGING)) ? "§2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Mining Fatigue I")
                .setEnchantmentGlow(containsEffect(PotionEffectType.SLOW_DIGGING)).build(),
                PotionEffectType.SLOW_DIGGING);
        
        BiConsumer<MenuClick, PotionEffectType> potionConsumer = (click, type) -> {
            changeSettings(click.getPlayer(), type);
            setItems();
        };
        
        buffFireResistanceItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.FIRE_RESISTANCE));
        buffJumpBoostItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.JUMP));
        buffHasteItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.FAST_DIGGING));
        buffRegenerationItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.REGENERATION));
        buffSpeedItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.SPEED));
        buffWaterBreathingItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.WATER_BREATHING));
        buffStrengthItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.INCREASE_DAMAGE));
        buffResistanceItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.DAMAGE_RESISTANCE));
        debuffMiningFatigueItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.SLOW_DIGGING));
        debuffSlownessItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.SLOW));
        debuffWeaknessItem.addClickAction(click -> potionConsumer.accept(click, PotionEffectType.WEAKNESS));
        
        set(9, buffFireResistanceItem);
        set(10, buffHasteItem);
        set(11, buffStrengthItem);
        set(18, buffRegenerationItem);
        set(19, buffWaterBreathingItem);
        set(20, buffJumpBoostItem);
        set(27, buffSpeedItem);
        set(28, buffResistanceItem);
        set(15, debuffSlownessItem);
        set(16, debuffWeaknessItem);
        set(17, debuffMiningFatigueItem);
    }
    
    private void changeSettings(Player player, PotionEffectType type) {
        //If the effect is a buff, then...
        if (type.equals(PotionEffectType.SLOW) || type.equals(PotionEffectType.SLOW_DIGGING) || type.equals(PotionEffectType.WEAKNESS)) {
            //It's a debuff. Set buffs to none.
            beacon.setBuff1(null);
            beacon.setBuff2(null);
            //If the effect == saved effect, set it to none.
            if (beacon.getDebuff() != null && beacon.getDebuff().getType().equals(type)) {
                beacon.setDebuff(null);
            } else {
                beacon.setDebuff(type);
            }
        } else {
            //It's a buff. Set debuffs to none.
            beacon.setDebuff(null);
            //If both slots are empty then set the effect to the first one.
            if (beacon.getBuff1() == null && beacon.getBuff2() == null)
                beacon.setBuff1(type);
                //If the first slot is not empty, then...
            else if (beacon.getBuff1() != null && beacon.getBuff2() == null)
                //If they are the same, first slot == none
                if (beacon.getBuff1().getType().equals(type))
                    beacon.setBuff1(null);
                    //If they are different, second slot is filled.
                else
                    beacon.setBuff2(type);
                //if the second slot is filled,
            else if (beacon.getBuff2() != null && beacon.getBuff1() == null)
                if (beacon.getBuff2().getType().equals(type))
                    beacon.setBuff2(null);
                else
                    beacon.setBuff1(type);
            else if (beacon.getBuff1().getType().equals(type))
                beacon.setBuff1(null);
            else if (beacon.getBuff2().getType().equals(type))
                beacon.setBuff2(null);
            else {
                MessageUtil.sendMessage(SuperBlocksPlugin.instance, player, MessageLevel.INFO, "Please de-select a buff before adding another.");
                return;
            }
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5F, 1F);
    }
    
    private boolean containsEffect(PotionEffectType type) {
        if (beacon.getBuff1() != null && beacon.getBuff1().getType().equals(type))
            return true;
        if (beacon.getBuff2() != null && beacon.getBuff2().getType().equals(type))
            return true;
        return (beacon.getDebuff() != null && beacon.getDebuff().getType().equals(type));
    }
    
    private static class PotionItem extends MenuItem {
        
        private PotionEffectType effect;
        
        public PotionItem(@Nullable ItemStack item, PotionEffectType effect) {
            super(item);
            
            this.effect = effect;
        }
        
        public PotionEffectType getEffect() {
            return effect;
        }
    }
}
