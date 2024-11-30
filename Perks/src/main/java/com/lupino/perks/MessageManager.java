package com.lupino.perks;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {
    private final File messagesFile;
    private final FileConfiguration messagesConfig;

    public MessageManager(JavaPlugin plugin) {

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            try {
                messagesFile.getParentFile().mkdirs();
                plugin.saveResource("messages.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }


    public String getMessage(String key) {
        String message = messagesConfig.getString(key);
        return (message != null ) ? ChatColor.translateAlternateColorCodes('&', message) : null;
    }

    public String getMessage(String key, Map<String, String> placeholders) {
        String message = getMessage(key);
        if (message == null ) {
            return null;
        }


        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        return message;
    }
}
