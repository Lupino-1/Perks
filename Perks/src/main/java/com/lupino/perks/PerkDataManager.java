package com.lupino.perks;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PerkDataManager {
    private final File dataFile;
    private final FileConfiguration dataConfig;

    public PerkDataManager(JavaPlugin plugin) {

        dataFile = new File(plugin.getDataFolder(), "perks.yml");


        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }


    public void setGems(UUID playerUUID, int points) {
        dataConfig.set(playerUUID.toString() + ".gems", points);
        saveData();
    }

    public int getGems(UUID playerUUID) {
        return dataConfig.getInt(playerUUID.toString() + ".gems", 0);
    }


    public void setPerkSlot(UUID playerUUID, int slot) {
        dataConfig.set(playerUUID.toString() + ".perkSlot", slot);
        saveData();
    }

    public int getPerkSlot(UUID playerUUID) {
        return dataConfig.getInt(playerUUID.toString() + ".perkSlot", 8); // Výchozí je poslední slot v hotbaru
    }
    public void setPlayerPerks(UUID playerUUID, List<ItemStack> perks) {
        dataConfig.set(playerUUID.toString() + ".perks", perks);
         saveData();
    }
    public void removePerkFromList(UUID playerUUID,ItemStack itemStack){
        List<ItemStack> perks = getPlayerPerks(playerUUID);
        perks.remove(itemStack);
        setPlayerPerks(playerUUID,perks);
    }
    public void addPerkToList(UUID playerUUID,ItemStack itemStack){
        List<ItemStack> perks = getPlayerPerks(playerUUID);
        perks.add(itemStack);
        setPlayerPerks(playerUUID,perks);


    }


    public List<ItemStack> getPlayerPerks(UUID playerUUID) {
        List<ItemStack> perks = (List<ItemStack>) dataConfig.get(playerUUID.toString() + ".perks");
        return perks != null ? perks : new ArrayList<>();
    }
    public void setCurrentPerk(UUID playerUUID, ItemStack currentPerk) {
        dataConfig.set(playerUUID.toString() + ".currentPerk", currentPerk);
        saveData();
    }

    public ItemStack getCurrentPerk(UUID playerUUID) {
        return (ItemStack) dataConfig.get(playerUUID.toString() + ".currentPerk");
    }




    private void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

