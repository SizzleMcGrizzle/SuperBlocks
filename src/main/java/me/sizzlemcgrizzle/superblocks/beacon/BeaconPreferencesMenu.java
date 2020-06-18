package me.sizzlemcgrizzle.superblocks.beacon;

import de.craftlancer.clclans.CLClans;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class BeaconPreferencesMenu extends Menu {
    //Buffs
    private final Button regenButton;
    private final Button strengthButton;
    private final Button speedButton;
    private final Button hasteButton;
    private final Button resistanceButton;
    private final Button jumpBoostButton;
    private final Button fireResistanceButton;
    private final Button waterBreathingButton;
    
    private final String strengthString = "strength";
    private final String regenString = "regen";
    private final String speedString = "speed";
    private final String hasteString = "haste";
    private final String resistanceString = "resistance";
    private final String jumpBoostString = "jumpboost";
    private final String fireResistanceString = "fireres";
    private final String waterBreathingString = "waterbreath";
    
    //Debuffs
    private final Button slownessButton;
    private final Button weaknessButton;
    private final Button miningFatigueButton;
    private final String slownessString = "slowness";
    private final String weaknessString = "weakness";
    private final String miningFatigueString = "minefatigue";
    
    //Misc
    private final Button durationButton;
    private final Button refreshButton;
    
    private ConfigurationSection configurationSection;
    private YamlConfiguration config;
    private File file;
    private CLClans clans = (CLClans) Bukkit.getPluginManager().getPlugin("CLClans");
    private String time;
    
    public BeaconPreferencesMenu(Location location) {
        this.file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/beacons.yml");
        this.config = new YamlConfiguration();
        
        try {
            String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();
            setTitle("&9Amplified Beacon Control Panel");
            setSound(new SimpleSound(Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1F, 2F));
            setSize(9 * 6);
            
            config.load(file);
            this.configurationSection = config.getConfigurationSection(serializedLocation);
            
            time = getTime();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        
        
        regenButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, regenString, true);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.POTION,
                        "&d&lRegeneration I",
                        (containsEffect(regenString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Regeneration I")
                        .glow((containsEffect(regenString)))
                        .build()
                        .make();
            }
        };
        strengthButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, strengthString, true);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.BLAZE_POWDER,
                        "&c&lStrength II",
                        (containsEffect(strengthString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Strength II")
                        .glow((containsEffect(strengthString)))
                        .build()
                        .make();
            }
        };
        speedButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, speedString, true);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SUGAR,
                        "&f&lSpeed II",
                        (containsEffect(speedString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Speed II")
                        .glow((containsEffect(speedString)))
                        .build()
                        .make();
            }
        };
        hasteButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, hasteString, true);
            }
            
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.GOLDEN_PICKAXE,
                        "&6&lHaste II",
                        (containsEffect(hasteString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Haste II")
                        .glow((containsEffect(hasteString)))
                        .build()
                        .make();
            }
        };
        resistanceButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, resistanceString, true);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.DIAMOND_CHESTPLATE,
                        "&9&lResistance II",
                        (containsEffect(resistanceString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Resistance II")
                        .glow((containsEffect(resistanceString)))
                        .build()
                        .make();
            }
        };
        jumpBoostButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, jumpBoostString, true);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.CHAINMAIL_BOOTS,
                        "&a&lJump Boost II",
                        (containsEffect(jumpBoostString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Jump Boost II")
                        .glow((containsEffect(jumpBoostString)))
                        .build()
                        .make();
            }
        };
        waterBreathingButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, waterBreathingString, true);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.WATER_BUCKET,
                        "&b&lWater Breathing",
                        (containsEffect(waterBreathingString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Water Breathing")
                        .glow((containsEffect(waterBreathingString)))
                        .build()
                        .make();
            }
        };
        fireResistanceButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, fireResistanceString, true);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.LAVA_BUCKET,
                        "&6&lFire Resistance",
                        (containsEffect(fireResistanceString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Fire Resistance")
                        .glow((containsEffect(fireResistanceString)))
                        .build()
                        .make();
            }
        };
        slownessButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, slownessString, false);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SOUL_SAND,
                        "&8&lSlowness I",
                        (containsEffect(slownessString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Slowness I")
                        .glow((containsEffect(slownessString)))
                        .build()
                        .make();
            }
        };
        weaknessButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, weaknessString, false);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.FIREWORK_STAR,
                        "&7&lWeakness I",
                        (containsEffect(weaknessString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Weakness I")
                        .glow((containsEffect(weaknessString)))
                        .build()
                        .make();
            }
        };
        miningFatigueButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                changeSettings(player, miningFatigueString, false);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.WOODEN_PICKAXE,
                        "&e&lMining Fatigue I",
                        (containsEffect(miningFatigueString)) ? "&2(selected)" : "",
                        "&7Set potion effect",
                        "&7to: Mining Fatigue I")
                        .glow((containsEffect(miningFatigueString)))
                        .build()
                        .make();
            }
        };
        durationButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                extendButtonClick(player);
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.NETHER_STAR,
                        "&5&lExtend Duration",
                        "",
                        "&7Click to extend time",
                        "",
                        "&6&nTime remaining:",
                        "",
                        time)
                        .build()
                        .make();
            }
        };
        refreshButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                time = getTime();
                restartMenu("&5Restarting Control Panel...");
            }
            
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PURPLE_STAINED_GLASS_PANE,
                        "&5&lRestart Control",
                        "&5&lPanel",
                        "",
                        "&7Click here to restart",
                        "&7control panel").build().make();
            }
            
        };
        
    }
    
    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 2)
            return ItemCreator.of(CompMaterial.GREEN_BANNER,
                    "&2&lBuffs",
                    "",
                    "&7Select/deselect an icon",
                    "&7to change buff effects",
                    "",
                    "&eYou can have &62&e buffs",
                    "&eand no debuffs.").build().make();
        if (slot == 6)
            return ItemCreator.of(CompMaterial.RED_BANNER,
                    "&c&lDebuffs",
                    "",
                    "&7Select/deselect an icon",
                    "&7to change debuff effects",
                    "",
                    "&eYou can have &61&e debuff",
                    "&eand no buffs.").build().make();
        if (slot == 9 * 1 + 1)
            return speedButton.getItem();
        if (slot == 9 * 1 + 2)
            return hasteButton.getItem();
        if (slot == 9 * 1 + 3)
            return jumpBoostButton.getItem();
        if (slot == 9 * 2 + 1)
            return resistanceButton.getItem();
        if (slot == 9 * 2 + 2)
            return strengthButton.getItem();
        if (slot == 9 * 2 + 3)
            return regenButton.getItem();
        if (slot == 9 * 3 + 1)
            return fireResistanceButton.getItem();
        if (slot == 9 * 3 + 2)
            return waterBreathingButton.getItem();
        if (slot == 9 * 1 + 5)
            return miningFatigueButton.getItem();
        if (slot == 9 * 1 + 6)
            return slownessButton.getItem();
        if (slot == 9 * 1 + 7)
            return weaknessButton.getItem();
        if (slot == 9 * 2 + 5 || slot == 9 * 2 + 6 || slot == 9 * 2 + 7)
            return null;
        if (slot == 9 * 3 + 3 || slot == 9 * 3 + 5 || slot == 9 * 3 + 6 || slot == 9 * 3 + 7)
            return null;
        if (slot == 9 * 5 + 3)
            return ItemCreator.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE,
                    "&5&lControl Panel Info",
                    "",
                    "&aBuffs&7 are only given",
                    "&7to &nclanmates&7. &cDebuffs",
                    "&7are only given to &nrivals&r&7.",
                    "",
                    "&eYou can only have &62",
                    "&ebuffs or &61&e debuff",
                    "&eat a time.").build().make();
        if (slot == 5 * 9 + 4)
            return durationButton.getItem();
        if (slot == 9 * 5 + 5)
            return refreshButton.getItem();
        /*if (slot % 9 == 0)
            return ItemCreator.of(CompMaterial.LIME_STAINED_GLASS_PANE, "").build().make();
        if (slot == 8 || slot == 17 || slot == 26 || slot == 35 || slot == 44 || slot == 53)
            return ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE, "").build().make();*/
        return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, "").build().make();
    }
    
    @Override
    protected String[] getInfo() {
        return null;
    }
    
    
    private boolean containsEffect(String effect) {
        if (getKey("buff1").equals(effect))
            return true;
        if (getKey("buff2").equals(effect))
            return true;
        return (getKey("debuff").equals(effect));
    }
    
    private boolean isOwnerOrInOwnerClan(Player player, UUID uuid) {
        if (!player.getUniqueId().equals(uuid)) {
            ///If the user is not the owner but the owner doesn't have a clan
            if (clans.getClan(Bukkit.getOfflinePlayer(uuid)) == null) {
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1f, 1F);
                Common.tell(player, Settings.PREFIX + "&cYou are not in the clan of the owner of this beacon!");
                player.closeInventory();
                return false;
            }
            //If the user is in the owner's clan
            if (!clans.getClan(Bukkit.getOfflinePlayer(uuid)).equals(clans.getClan(Bukkit.getOfflinePlayer(player.getUniqueId())))) {
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1f, 1F);
                Common.tell(player, Settings.PREFIX + "&cYou are not in the same clan as the owner of this beacon!");
                player.closeInventory();
                return false;
            }
        }
        return true;
    }
    
    private void changeSettings(Player player, String effect, Boolean isBuff) {
        SuperBlocksPlugin superBlocks = (SuperBlocksPlugin) Bukkit.getPluginManager().getPlugin("SuperBlocks");
        UUID uuid = UUID.fromString(configurationSection.getString("playerUUID"));
        if (!isOwnerOrInOwnerClan(player, uuid))
            return;
        try {
            //If the effect is a buff, then...
            if (!isBuff) {
                //It's a debuff. Set buffs to none.
                setKey("buff1", "none");
                setKey("buff2", "none");
                //If the effect == saved effect, set it to none.
                if (getKey("debuff").equals(effect)) {
                    setKey("debuff", "none");
                } else {
                    setKey("debuff", effect);
                }
            } else {
                //It's a buff. Set debuffs to none.
                setKey("debuff", "none");
                //If both slots are empty then set the effect to the first one.
                if (getKey("buff1").equals("none") && getKey("buff2").equals("none")) {
                    setKey("buff1", effect);
                    //If the first slot is not empty, then...
                } else if (!getKey("buff1").equals("none") && getKey("buff2").equals("none")) {
                    //If they are the same, first slot == none
                    if (getKey("buff1").equals(effect)) {
                        setKey("buff1", "none");
                        //If they are different, second slot is filled.
                    } else {
                        setKey("buff2", effect);
                    }
                    //if the second slot is filled,
                } else if (!getKey("buff2").equals("none") && getKey("buff1").equals("none")) {
                    if (getKey("buff2").equals(effect)) {
                        setKey("buff2", "none");
                    } else {
                        setKey("buff1", effect);
                    }
                } else {
                    if (getKey("buff1").equals(effect)) {
                        setKey("buff1", "none");
                    } else if (getKey("buff2").equals(effect)) {
                        setKey("buff2", "none");
                    } else
                        Common.tell(getViewer(), Settings.PREFIX + "&cPlease de-select a buff before adding another.");
                }
            }
            config.save(file);
            superBlocks.cacheBeacons();
            restartMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getKey(String value) {
        return ((String) configurationSection.get(value));
    }
    
    private void setKey(String key, String value) {
        getViewer().playSound(getViewer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 2F);
        configurationSection.set(key, value);
    }
    
    private void extendTime() {
        SuperBlocksPlugin superBlocks = (SuperBlocksPlugin) Bukkit.getPluginManager().getPlugin("SuperBlocks");
        
        try {
            long time = configurationSection.getLong("expires");
            if (time == 0 || System.currentTimeMillis() > time)
                configurationSection.set("expires", System.currentTimeMillis() + 604800000);
            else
                configurationSection.set("expires", time + 604800000);
            config.save(file);
            superBlocks.cacheBeacons();
        } catch (IOException | NullPointerException e) {
            Common.tell(getViewer(), Settings.PREFIX + "&cThis beacon is broken... Contact an admin for assistance.");
        }
    }
    
    private String getTime() {
        long expires = configurationSection.getLong("expires");
        long difference = expires - System.currentTimeMillis();
        
        if (expires == 0)
            return "&cInactive";
        
        int months, days, hours, minutes, seconds;
        
        months = (int) (difference / 2628000000L);
        difference -= months * 2628000000L;
        days = (int) (difference / 86400000L);
        difference -= days * 86400000L;
        hours = (int) (difference / 3600000L);
        difference -= hours * 3600000L;
        minutes = (int) (difference / 60000L);
        difference -= minutes * 60000L;
        seconds = (int) (difference / 1000);
        
        return "&e" + Integer.max(months, 0) + "m&7-&e" + Integer.max(days, 0) + "d&7-&e" + Integer.max(hours, 0) + "h&7-&e" + Integer.max(minutes, 0) + "m&7-&e" + Integer.max(seconds, 0) + "s";
    }
    
    private void extendButtonClick(Player player) {
        UUID uuid = UUID.fromString(configurationSection.getString("playerUUID"));
        if (!isOwnerOrInOwnerClan(player, uuid))
            return;
        
        if (Settings.USE_ECONOMY) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
            
            if (!SuperBlocksPlugin.getEconomy().has(offlinePlayer, Settings.CURRENCY_MONEY)) {
                Common.tell(player, Settings.PREFIX + "&cYou do not have $" + Settings.CURRENCY_MONEY + " to pay!");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 0.5F);
            } else {
                SuperBlocksPlugin.getEconomy().withdrawPlayer(offlinePlayer, Settings.CURRENCY_MONEY);
                Common.tell(player, Settings.PREFIX + "&aYou have extended the power of this beacon by 1 week.");
                extendTime();
                restartMenu("&5Added one week of power");
            }
        } else {
            Inventory inventory = player.getInventory();
            ItemStack clone;
            for (ItemStack item : inventory.getContents()) {
                if (item == null || item.getType().equals(Material.AIR))
                    continue;
                clone = item.clone();
                clone.setAmount(1);
                if (clone.isSimilar(Settings.CURRENCY_ITEM)) {
                    item.setAmount(item.getAmount() - 1);
                    player.updateInventory();
                    Common.tell(player, Settings.PREFIX + "&aYou have extended the power of this beacon by 1 week.");
                    extendTime();
                    restartMenu("&5Added one week of power");
                    return;
                }
            }
            
            SimpleComponent
                    .of(Settings.PREFIX)
                    .append("&cYou do not have one ")
                    .append(Settings.CURRENCY_ITEM.getItemMeta().getDisplayName())
                    .onHover(Settings.CURRENCY_ITEM)
                    .append(" &r&cto &cpay!")
                    .send(getViewer());
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 0.5F);
        }
    }
    
    
}
