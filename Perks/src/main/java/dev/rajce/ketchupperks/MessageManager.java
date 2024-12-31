package dev.rajce.ketchupperks;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class MessageManager {
    private final File messagesFile;
    private FileConfiguration messagesConfig;

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
        return (message != null ) ? translateColors(message) : null;
    }
public  String replacePlaceholders (String message,Map<String, String> placeholders){

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
    public String translateColors(String message) {
        // 1. Nahrazení hex barev (&#xxxxxx na §x§R§G§B...)
        message = message.replaceAll("&#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])",
                "§x§$1§$2§$3§$4§$5§$6");

        // 2. Překlad '&' barevných kódů na '§'
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void loadMessagesConfig() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }


    public void reloadMessages() {
        if (!messagesFile.exists()) {
            try {
                messagesFile.getParentFile().mkdirs();
                messagesFile.createNewFile();
                loadMessagesConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loadMessagesConfig();
        }
    }


}
