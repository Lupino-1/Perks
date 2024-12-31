package dev.rajce.ketchupperks.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.rajce.ketchupperks.Keys;
import dev.rajce.ketchupperks.MessageManager;
import dev.rajce.ketchupperks.PerkDataManager;
import dev.rajce.ketchupperks.Perks;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class OpenPerkMenuCommand implements CommandExecutor {

    private final PerkDataManager perkDataManager;
    private final Perks plugin;
    private final MessageManager messageManager;

    public OpenPerkMenuCommand(PerkDataManager perkDataManager, Perks plugin, MessageManager messageManager) {
        this.perkDataManager = perkDataManager;
        this.plugin = plugin;
        this.messageManager = messageManager;

    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;
            if (!isInAnySafeZone(player)) {

                String message = messageManager.getMessage("not-at-spawn-message");
                if (message != null) {
                    player.sendMessage(message);
                }

                return false;
            }
            openMenu(player);

        }


        return true;
    }

    public void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.BLACK + "Perks");
        putEdgeItems(inventory);

        setNameAndGiveToInvWithPersi(
                plugin.getConfig().getString("buttons.perk-shop-button.material", "GOLDEN_AXE"),
                messageManager.translateColors(plugin.getConfig().getString("buttons.perk-shop-button.name", "Perks shop")),
                plugin.getConfig().getInt("buttons.perk-shop-button.slot", 31),
                inventory,
                Keys.PERK_SHOP_BUTTON,
                createLore(plugin.getConfig().getStringList("buttons.perk-shop-button.lore")),
                plugin.getConfig().getInt("buttons.perk-shop-button.model-data", 0));

        setNameAndGiveToInvWithPersi(
                plugin.getConfig().getString("buttons.perk-choose-button.material", "DIAMOND_AXE"),
                messageManager.translateColors(plugin.getConfig().getString("buttons.perk-choose-button.name", "Vybrat perk")),
                plugin.getConfig().getInt("buttons.perk-choose-button.slot", 29),
                inventory,
                Keys.PERK_CHOOSE_BUTTON,
                createLore(plugin.getConfig().getStringList("buttons.perk-choose-button.lore")),
                plugin.getConfig().getInt("buttons.perk-choose-button.model-data", 0));


        setNameAndGiveToInvWithPersi(plugin.getConfig().getString("buttons.slot-choose-button.material","GLASS_PANE"),
                messageManager.translateColors(plugin.getConfig().getString("buttons.slot-choose-button.name", "Vybrat slot")),
                plugin.getConfig().getInt("buttons.slot-choose-button.slot",33),
                inventory,
                Keys.SLOT,
                createLore(plugin.getConfig().getStringList("buttons.slot-choose-button.lore")),
                plugin.getConfig().getInt("buttons.slot-choose-button.model-data",0));
        //addItemsToInventoryMenu(inventory,perkDataManager.getPlayerPerks(player.getUniqueId()),0,player);


        player.openInventory(inventory);

    }

    public void putEdgeItems(Inventory inventory) {
        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta redGlassMeta = redGlass.getItemMeta();
        redGlassMeta.setDisplayName(" ");
        redGlass.setItemMeta(redGlassMeta);

        ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackGlassMeta = blackGlass.getItemMeta();
        blackGlassMeta.setDisplayName(" ");
        blackGlass.setItemMeta(blackGlassMeta);


        for (int i = 0; i < 54; i++) {
            if (isRedEdge(i)) {
                inventory.setItem(i, redGlass);

            }
            if (isBlackEdge(i)) {
                inventory.setItem(i, blackGlass);

            }
        }


    }


    private boolean isRedEdge(int index) {

        if (index == 1 || index == 7) return true;

        if (index == 46 || index == 52) return true;

        if (index % 9 == 0) return true;

        if ((index + 1) % 9 == 0) return true;

        return false;
    }

    private boolean isBlackEdge(int index) {

        if (index > 1 && index < 7) return true;

        if (index > 46 && index < 52) return true;

        return false;
    }

    public void setNameAndGiveToInv(String material, String name, Integer slot, Inventory inventory, List<String> lore, int modelData) {
        ItemStack itemStack = createItem(material, name, lore, modelData);


        inventory.setItem(slot, itemStack);

    }

    public void setNameAndGiveToInvWithPersi(String material, String name, Integer slot, Inventory inventory, NamespacedKey key, List<String> lore, int modeldata) {

        ItemStack itemStack = createItemWithPersi(material, name, lore, modeldata,key);
        inventory.setItem(slot, itemStack);


    }

    public List<String> createLore(List<String> configLore) {
        List<String> lore = new ArrayList<>();
        if (configLore != null) {
            for (String line : configLore) {
                lore.add(messageManager.translateColors(line));
            }
        }

        return lore;
    }

    public void addItemsToInventoryMenu(Inventory inventory, List<ItemStack> items, Integer startingIndex, Player player) {


        for (ItemStack item : items) {
            inventory.remove(item);
        }

        inventory.setItem(plugin.getConfig().getInt("buttons.none-perk-button.slot",31),
                createItemWithPersi(plugin.getConfig().getString("buttons.none-perk-button.material", "GRAY_DYE"),
                messageManager.translateColors(plugin.getConfig().getString("buttons.none-perk-button.name","Nevybraný perk")),
                createLore(plugin.getConfig().getStringList("buttons.none-perk-button.lore")),
                plugin.getConfig().getInt("buttons.none-perk-button.model-data",0),
                        Keys.NONE_PERK)
        );

        //inventory.setItem(31, createItemWithNameAndPersi(material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK));

        for (ItemStack item : items) {
            for (int i = startingIndex; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {

                    ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());
                    if (currentPerk != null && item.equals(currentPerk)) {
                        inventory.setItem(plugin.getConfig().getInt("buttons.none-perk-button.slot",31), item);

                        break;
                    }
                    inventory.setItem(i, item);
                    break;
                }
            }
        }


        ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());
        if (currentPerk != null && currentPerk.getItemMeta() != null && currentPerk.getItemMeta().getPersistentDataContainer().has(Keys.NONE_PERK)) {
            inventory.setItem(plugin.getConfig().getInt("buttons.none-perk-button.slot",31),
                    createItemWithPersi(plugin.getConfig().getString("buttons.none-perk-button.material", "GRAY_DYE"),
                            messageManager.translateColors(plugin.getConfig().getString("buttons.none-perk-button.name","Nevybraný perk")),
                            createLore(plugin.getConfig().getStringList("buttons.none-perk-button.lore")),
                            plugin.getConfig().getInt("buttons.none-perk-button.model-data",0),
                            Keys.NONE_PERK)
            );


        }

    }

    public void addItemsToInventory(Inventory inventory, List<ItemStack> items, int startingIndex) {


        for (ItemStack item : items) {

            for (int i = startingIndex; i < inventory.getSize(); i++) {

                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, item);
                    break;
                }
            }

        }


    }

    public ItemStack createItemWithNameAndPersi(Material material, String name, NamespacedKey key) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(meta);


        return itemStack;
    }

    public boolean isPerkItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            for (NamespacedKey key : getAllPerkKeys()) {

                if (itemMeta.getPersistentDataContainer().has(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<NamespacedKey> getAllPerkKeys() {
        return List.of(Keys.SCOUT, Keys.RUNNER, Keys.JUMPER, Keys.RUNNER, Keys.MINER, Keys.KNIGHT, Keys.THOR, Keys.TANK, Keys.VAMPIRE);
    }

    public boolean isInSafeZone(Player player, Location loc1, Location loc2) {

        Location playerLoc = player.getLocation();

        double minX = Math.min(loc1.getX(), loc2.getX());
        double maxX = Math.max(loc1.getX(), loc2.getX());

        double minY = Math.min(loc1.getY(), loc2.getY());
        double maxY = Math.max(loc1.getY(), loc2.getY());

        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());


        return playerLoc.getX() >= minX && playerLoc.getX() <= maxX &&
                playerLoc.getY() >= minY && playerLoc.getY() <= maxY &&
                playerLoc.getZ() >= minZ && playerLoc.getZ() <= maxZ;
    }

    public boolean isInAnySafeZone(Player player) {
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("safezone.overworld.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("safezone.overworld.loc2")))) {
            return true;
        }
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("safezone.nether.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("safezone.nether.loc2")))) {
            return true;
        }
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("safezone.end.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("safezone.end.loc2")))) {
            return true;
        }
        return false;
    }



    public ItemStack createItemWithPersi(String material, String name, List<String> lore, int modelData, NamespacedKey key) {
        ItemStack item;

        if (material.startsWith("texture-")) {
            // Vytvoření hlavy hráče s texturou
            item = new ItemStack(Material.PLAYER_HEAD);
           // System.out.println("Detected texture: " + material);
            try {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                if (skullMeta != null) {
                    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                    PlayerTextures textures = profile.getTextures();

                    try {
                        // Nastavení textury z URL
                        URL urlObject = new URL("https://textures.minecraft.net/texture/" + material.substring(8));
                        textures.setSkin(urlObject);
                        //System.out.println("bylo nastavena textura " + urlObject);
                    } catch (MalformedURLException ignored) {
                    }

                    profile.setTextures(textures);
                    skullMeta.setPlayerProfile(profile);

                    if (name != null && !name.isEmpty()) {
                        skullMeta.setDisplayName(name);
                    }

                    if (lore != null && !lore.isEmpty()) {
                        skullMeta.setLore(lore);
                    }

                    if (modelData > 0) {
                        skullMeta.setCustomModelData(modelData);
                    }

                    item.setItemMeta(skullMeta);
                }
            } catch (ClassCastException ignored) {
            }

        } else {
            // Vytvoření běžného itemu
            Material mat = Material.getMaterial(material.toUpperCase());
            if (mat == null) {
                throw new IllegalArgumentException("Invalid material: " + material);
            }

            item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                if (name != null && !name.isEmpty()) {
                    meta.setDisplayName(name);
                }

                if (lore != null && !lore.isEmpty()) {
                    meta.setLore(lore);
                }

                if (modelData > 0) {
                    meta.setCustomModelData(modelData);
                }

                item.setItemMeta(meta);
            }
        }
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack createItem(String material, String name, List<String> lore, int modelData) {
        ItemStack item;

        if (material.startsWith("texture-")) {
            // Vytvoření hlavy hráče s texturou
            item = new ItemStack(Material.PLAYER_HEAD);
            // System.out.println("Detected texture: " + material);
            try {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                if (skullMeta != null) {
                    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                    PlayerTextures textures = profile.getTextures();

                    try {
                        // Nastavení textury z URL
                        URL urlObject = new URL("https://textures.minecraft.net/texture/" + material.substring(8));
                        textures.setSkin(urlObject);
                        //System.out.println("bylo nastavena textura " + urlObject);
                    } catch (MalformedURLException ignored) {
                    }

                    profile.setTextures(textures);
                    skullMeta.setPlayerProfile(profile);

                    if (name != null && !name.isEmpty()) {
                        skullMeta.setDisplayName(name);
                    }

                    if (lore != null && !lore.isEmpty()) {
                        skullMeta.setLore(lore);
                    }

                    if (modelData > 0) {
                        skullMeta.setCustomModelData(modelData);
                    }

                    item.setItemMeta(skullMeta);
                }
            } catch (ClassCastException ignored) {
            }

        } else {
            // Vytvoření běžného itemu
            Material mat = Material.getMaterial(material.toUpperCase());
            if (mat == null) {
                throw new IllegalArgumentException("Invalid material: " + material);
            }

            item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                if (name != null && !name.isEmpty()) {
                    meta.setDisplayName(name);
                }

                if (lore != null && !lore.isEmpty()) {
                    meta.setLore(lore);
                }

                if (modelData > 0) {
                    meta.setCustomModelData(modelData);
                }

                item.setItemMeta(meta);
            }
        }

        return item;
    }
   }


