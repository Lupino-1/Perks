package com.lupino.perks.Commands;

import com.lupino.perks.Keys;
import com.lupino.perks.MessageManager;
import com.lupino.perks.PerkDataManager;
import com.lupino.perks.Perks;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OpenPerkMenuCommand implements CommandExecutor {

    private final PerkDataManager perkDataManager;
    private final Perks plugin;
    private final MessageManager messageManager;

    public OpenPerkMenuCommand(PerkDataManager perkDataManager,Perks plugin,MessageManager messageManager) {
        this.perkDataManager = perkDataManager;
        this.plugin=plugin;
        this.messageManager=messageManager;

    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;
            if(!isInAnySafeZone(player)){

                String message = messageManager.getMessage("NotAtSpawnMessage");
                if(message!=null){
                    player.sendMessage(message);
                }

                return false;
            }
            OpenMenu(player);

        }


        return true;
    }

    public void OpenMenu(Player player){
        Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.BLACK+"Perks");
        putEdgeItems(inventory);
        setNameAndGivetoinvWithPersi(Material.GOLDEN_AXE,ChatColor.DARK_RED+"Perks shop",31,inventory,Keys.PERKSHOPBUTTON);
        setNameAndGivetoinv(Material.GRAY_DYE,"Nevybraný perk",29,inventory);
        setNameAndGivetoinvWithPersi(Material.GLASS_PANE,ChatColor.DARK_RED+ "Vybrat slot",33,inventory,Keys.SLOT);
        addItemsToInventoryMenu(inventory,perkDataManager.getPlayerPerks(player.getUniqueId()),0,player);



        player.openInventory(inventory);

    }

    public void putEdgeItems(Inventory inventory){
        ItemStack Redglass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta RedglassMeta = Redglass.getItemMeta();
        RedglassMeta.setDisplayName(" ");
        Redglass.setItemMeta(RedglassMeta);

        ItemStack Blackglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta BlackglassMeta = Blackglass.getItemMeta();
        BlackglassMeta.setDisplayName(" ");
        Blackglass.setItemMeta(BlackglassMeta);


        for (int i = 0; i < 54; i++) {
            if (isRedEdge(i)) {
                inventory.setItem(i,Redglass);

            }
            if (isBlackEdge(i)){
                inventory.setItem(i,Blackglass);

            }
        }


    }



    private boolean isRedEdge(int index) {

        if (index == 1||index==7) return true;

        if (index == 46||index==52) return true;

        if (index % 9 == 0) return true;

        if ((index + 1) % 9 == 0) return true;

        return false;
    }
    private boolean isBlackEdge(int index) {

        if (index > 1&& index<7) return true;

        if (index > 46&& index<52) return true;

        return false;
    }
    public void setNameAndGivetoinv(Material material, String name, Integer slot, Inventory inventory){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta= itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        inventory.setItem(slot,itemStack);

    }
    public void setNameAndGivetoinvWithPersi(Material material, String name, Integer slot, Inventory inventory, NamespacedKey key){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta= itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.getPersistentDataContainer().set(key,PersistentDataType.BOOLEAN,true);
        itemStack.setItemMeta(meta);
        inventory.setItem(slot,itemStack);






    }


    public void addItemsToInventoryMenu(Inventory inventory, List<ItemStack> items,Integer startingIndex,Player player) {


        for (ItemStack item : items) {
            inventory.remove(item);
        }

        
        for (ItemStack item : items) {
            for (int i = startingIndex; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());
                    if (currentPerk != null && item.equals(currentPerk)) {
                        inventory.setItem(29, item);
                        break;
                    }
                    inventory.setItem(i, item);
                    break;
                }
            }
        }


        ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());
        if (currentPerk != null && currentPerk.getItemMeta() != null && currentPerk.getItemMeta().getPersistentDataContainer().has(Keys.NONEPERK)) {

            inventory.setItem(29, CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK));
        }
    }
    public void addItemsToInventory(Inventory inventory, List<ItemStack> items,Integer startingindex) {


        for (ItemStack item : items) {

            for (int i = startingindex; i < inventory.getSize(); i++) {

                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, item);
                    break;
                }
            }

        }


    }

    public ItemStack CreateItemWithNameandpersi(Material material,String name,NamespacedKey key){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta= itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.getPersistentDataContainer().set(key,PersistentDataType.BOOLEAN,true);
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
        return List.of(Keys.SCOUT,Keys.RUNNER, Keys.JUMPER, Keys.RUNNER, Keys.MINER, Keys.KNIGHT, Keys.THOR,Keys.TANK);
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
    public boolean isInAnySafeZone (Player player) {
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Overworld.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Overworld.loc2")))) {
            return true;
        }
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Nether.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Nether.loc2")))) {
            return true;
        }
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.End.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.End.loc2")))) {
            return true;
        }
        return false;
    }
}
