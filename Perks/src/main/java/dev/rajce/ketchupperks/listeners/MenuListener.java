package dev.rajce.ketchupperks.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;

import dev.rajce.ketchupperks.Keys;
import dev.rajce.ketchupperks.MessageManager;
import dev.rajce.ketchupperks.commands.OpenPerkMenuCommand;
import dev.rajce.ketchupperks.PerkDataManager;
import dev.rajce.ketchupperks.Perks;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MenuListener implements Listener {
    private final PerkDataManager perkDataManager;
    private final OpenPerkMenuCommand openPerkMenuCommand;


    private final MessageManager messageManager;

    private final Perks plugin;

    public MenuListener(PerkDataManager perkDataManager,OpenPerkMenuCommand openPerkMenuCommand,MessageManager messageManager,Perks plugin) {
        this.perkDataManager = perkDataManager;
        this.openPerkMenuCommand= openPerkMenuCommand;
        this.messageManager=messageManager;
        this.plugin= plugin;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        ItemStack hand = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        ItemStack cursor = event.getCursor();
        int hotbarKey = event.getHotbarButton(); // Vrací číslo klávesy (0-8) nebo -1

        // Zamezení přesunu perku klávesovou zkratkou
        if (hotbarKey != -1) {
            ItemStack hotbarItem = player.getInventory().getItem(hotbarKey);

            // Pokud je předmět v hotbaru nebo na slotu perk
            if (openPerkMenuCommand.isPerkItem(hotbarItem) || openPerkMenuCommand.isPerkItem(hand)) {
                event.setCancelled(true); // Zamezení akce


            }
        }



        if (hand!= null && openPerkMenuCommand.isPerkItem(hand)&& perkDataManager.getCurrentPerk(player.getUniqueId())!=null&&!perkDataManager.getCurrentPerk(player.getUniqueId()).equals(openPerkMenuCommand.createItemWithNameAndPersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONE_PERK))) {

            event.setCancelled(true);
            player.getInventory().remove(hand);
            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()),perkDataManager.getCurrentPerk(player.getUniqueId()));

        }
        if (cursor!= null && openPerkMenuCommand.isPerkItem(cursor)&&!perkDataManager.getCurrentPerk(player.getUniqueId()).equals(openPerkMenuCommand.createItemWithNameAndPersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONE_PERK))) {

            event.setCancelled(true);
            player.getInventory().remove(cursor);
            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()),perkDataManager.getCurrentPerk(player.getUniqueId()));

        }
        if (event.getClick().isKeyboardClick() && openPerkMenuCommand.isPerkItem(cursor)||openPerkMenuCommand.isPerkItem(hand)) {

            event.setCancelled(true);


        }



        if(event.getView().getTitle().equals(ChatColor.BLACK+"Perks")){
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.PERK_CHOOSE_BUTTON)) {
                Inventory perk = Bukkit.createInventory(player, 54, ChatColor.BLACK+"Perk");
                openPerkMenuCommand.putEdgeItems(perk);
                openPerkMenuCommand.addItemsToInventoryMenu(perk,perkDataManager.getPlayerPerks(player.getUniqueId()),0,player);

                openPerkMenuCommand.setNameAndGiveToInvWithPersi(
                        plugin.getConfig().getString("buttons.close-button.material", "BARRIER"),
                        messageManager.translateColors(plugin.getConfig().getString("buttons.close-button.name", "Back")),
                        plugin.getConfig().getInt("buttons.close-button.slot", 50),
                        perk,
                        Keys.CLOSE_BUTTON,
                        openPerkMenuCommand.createLore(plugin.getConfig().getStringList("buttons.close-button.lore")),
                        plugin.getConfig().getInt("buttons.close-button.model-data", 0));

                openPerkMenuCommand.setNameAndGiveToInvWithPersi(
                        plugin.getConfig().getString("buttons.back-button.material", "texture-66d8eff4c673e0636907ea5c0b5ff4f64dc35c6aad9b797f1df663351b4c0814"),
                        messageManager.translateColors(plugin.getConfig().getString("buttons.back-button.name", "Back")),
                        plugin.getConfig().getInt("buttons.back-button.slot", 48),
                        perk,
                        Keys.BACK,
                        openPerkMenuCommand.createLore(plugin.getConfig().getStringList("buttons.back-button.lore")),
                        plugin.getConfig().getInt("buttons.back-button.model-data", 0));

                player.openInventory(perk);


            }


            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.PERK_SHOP_BUTTON)) {
                Inventory perkshop = Bukkit.createInventory(player, 54, ChatColor.BLACK+"Perks shop");
                openPerkMenuCommand.putEdgeItems(perkshop);
                List<ItemStack> listofallperks = createListOfAllPerks();
                openPerkMenuCommand.addItemsToInventory(perkshop,listofallperks,10);

                openPerkMenuCommand.setNameAndGiveToInvWithPersi(
                        plugin.getConfig().getString("buttons.close-button.material", "BARRIER"),
                        messageManager.translateColors(plugin.getConfig().getString("buttons.close-button.name", "Back")),
                        plugin.getConfig().getInt("buttons.close-button.slot", 50),
                        perkshop,
                        Keys.CLOSE_BUTTON,
                        openPerkMenuCommand.createLore(plugin.getConfig().getStringList("buttons.close-button.lore")),
                        plugin.getConfig().getInt("buttons.close-button.model-data", 0));

                openPerkMenuCommand.setNameAndGiveToInvWithPersi(
                        plugin.getConfig().getString("buttons.back-button.material", "texture-66d8eff4c673e0636907ea5c0b5ff4f64dc35c6aad9b797f1df663351b4c0814"),
                        messageManager.translateColors(plugin.getConfig().getString("buttons.back-button.name", "Back")),
                        plugin.getConfig().getInt("buttons.back-button.slot", 48),
                        perkshop,
                        Keys.BACK,
                        openPerkMenuCommand.createLore(plugin.getConfig().getStringList("buttons.back-button.lore")),
                        plugin.getConfig().getInt("buttons.back-button.model-data", 0));

                openPerkMenuCommand.setNameAndGiveToInv(plugin.getConfig().getString("buttons.your-gems-button.material","SUNFLOWER"),
                        messageManager.replacePlaceholders(   messageManager.translateColors(plugin.getConfig().getString("buttons.your-gems-button.name","Your gems: {gems}")),Map.of("gems",String.valueOf(perkDataManager.getGems(player.getUniqueId())))),
                        plugin.getConfig().getInt("buttons.your-gems-button.slot",31),
                        perkshop,
                        openPerkMenuCommand.createLore(plugin.getConfig().getStringList("buttons.your-gems-button.lore")),
                        plugin.getConfig().getInt("buttons.your-gems-button.model-data",0));

                player.openInventory(perkshop);


            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.SLOT)){
                Inventory slot = Bukkit.createInventory(player, 9, ChatColor.BLACK+"Inventory slot");

                openPerkMenuCommand.setNameAndGiveToInv(plugin.getConfig().getString("buttons.your-slot-button.material", "GLASS_PANE"),
                        messageManager.translateColors(plugin.getConfig().getString("buttons.your-slot-button.name", "Tvůj slot")),
                        perkDataManager.getPerkSlot(player.getUniqueId()),
                        slot,
                        openPerkMenuCommand.createLore(plugin.getConfig().getStringList("buttons.your-slot-button.lore")),plugin.getConfig().getInt("buttons.your-slot-button.model-data",0));

                player.openInventory(slot);




            }

            event.setCancelled(true);
        }
        if(event.getView().getTitle().equals(ChatColor.BLACK+"Perk")){

            if (hand != null && hand.hasItemMeta() && openPerkMenuCommand.isPerkItem(hand)){
                setPerk(hand,player,event.getInventory());

            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.BACK)){


                openPerkMenuCommand.openMenu(player);
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.CLOSE_BUTTON)){


                player.closeInventory();
            }

            event.setCancelled(true);
        }




        if(event.getView().getTitle().equals(ChatColor.BLACK+"Perks shop")){
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.SCOUT)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("scout.price", 10), player, event.getCurrentItem(),"ketchupperks.scout");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.JUMPER)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("jumper.price", 10), player, event.getCurrentItem(),"ketchupperks.jumper");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.RUNNER)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("runner.price", 10), player, event.getCurrentItem(),"ketchupperks.runner");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.MINER)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("miner.price", 10), player, event.getCurrentItem(),"ketchupperks.miner");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.KNIGHT)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("knight.price", 10), player, event.getCurrentItem(),"ketchupperks.knight");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.THOR)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("thor.price", 10), player, event.getCurrentItem(),"ketchupperks.thor");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.TANK)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("tank.price", 10), player, event.getCurrentItem(),"ketchupperks.tank");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.VAMPIRE)) {
                doAllChecksForPurchaseAndGivePlayerPerk(plugin.getConfig().getInt("vampire.price", 10), player, event.getCurrentItem(),"ketchupperks.vampire");
            }


            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.BACK)){

                openPerkMenuCommand.openMenu(player);
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.CLOSE_BUTTON)){


                player.closeInventory();
            }




        event.setCancelled(true);
        }







        if(event.getView().getTitle().equals(ChatColor.BLACK+"Inventory slot")){




            perkDataManager.setPerkSlot(player.getUniqueId(),event.getSlot());
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
            String message = messageManager.getMessage("slot-message",Map.of("slot",String.valueOf(perkDataManager.getPerkSlot(player.getUniqueId())+1)));
            if(message!=null){
                player.sendMessage(message);
            }
            changeSlot(player);



            event.setCancelled(true);
            player.closeInventory();
        }





    }








public ItemStack createPerk(String material, String name, NamespacedKey namespacedKey, Integer numberofusages, Integer price, List<String> configLore, int modelData){
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

        }

    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    List<String> lore = new ArrayList<>();
    lore.add(ChatColor.WHITE + "Počet použití " + numberofusages);


    // Přidáme lore z configu a přeložíme barvy
    if (configLore != null) {
        for (String line : configLore) {
            lore.add(messageManager.translateColors(line));
        }
        lore.add("");
    }
    lore.add(ChatColor.translateAlternateColorCodes('&',"&4| &f• ")+ ChatColor.GRAY + "Cena: " + price + " gems");
    lore.add("");
    lore.add(ChatColor.translateAlternateColorCodes('&',"&4\uA48F &f> &7Koupit Perk"));
    meta.setLore(lore);
    meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER,numberofusages);
    meta.setUnbreakable(true);
    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    if (modelData > 0) {
        meta.setCustomModelData(modelData);
    }
    item.setItemMeta(meta);
    return item;
}
    public List<ItemStack> createListOfAllPerks( ) {
        List<ItemStack> perks = new ArrayList<>();
        if (plugin.getConfig().getBoolean("scout.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("scout.material","FISHING_ROD"),
                    messageManager.translateColors(plugin.getConfig().getString("scout.name", "scout")),
                    Keys.SCOUT,
                    plugin.getConfig().getInt("scout.usages", 5),
                    plugin.getConfig().getInt("scout.price", 10),
                    plugin.getConfig().getStringList("scout.lore"),
                    plugin.getConfig().getInt("scout.model-data", 0)));
        }
        if (plugin.getConfig().getBoolean("jumper.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("jumper.material", "FEATHER"),
                    messageManager.translateColors(plugin.getConfig().getString("jumper.name", "jumper")),
                    Keys.JUMPER,
                    plugin.getConfig().getInt("jumper.usages", 5),
                    plugin.getConfig().getInt("jumper.price", 10),
                    plugin.getConfig().getStringList("jumper.lore"),
                    plugin.getConfig().getInt("jumper.model-data", 0)));
        }
        if (plugin.getConfig().getBoolean("runner.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("runner.material", "SUGAR"),
                    messageManager.translateColors(plugin.getConfig().getString("runner.name", "runner")),
                    Keys.RUNNER,
                    plugin.getConfig().getInt("runner.usages", 5),
                    plugin.getConfig().getInt("runner.price", 10),
                    plugin.getConfig().getStringList("runner.lore"),
                    plugin.getConfig().getInt("runner.model-data", 0)));
        }
        if (plugin.getConfig().getBoolean("miner.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("miner.material", "IRON_PICKAXE"),
                    messageManager.translateColors(plugin.getConfig().getString("miner.name", "miner")),
                    Keys.MINER,
                    plugin.getConfig().getInt("miner.usages", 10),
                    plugin.getConfig().getInt("miner.price", 10),
                    plugin.getConfig().getStringList("miner.lore"),
                    plugin.getConfig().getInt("miner.model-data", 0)));
        }
        if (plugin.getConfig().getBoolean("knight.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("knight.material", "IRON_CHESTPLATE"),
                    messageManager.translateColors(plugin.getConfig().getString("knight.name", "knight")),
                    Keys.KNIGHT,
                    plugin.getConfig().getInt("knight.usages", 5),
                    plugin.getConfig().getInt("knight.price", 10),
                    plugin.getConfig().getStringList("knight.lore"),
                    plugin.getConfig().getInt("knight.model-data", 0)));
        }
        if (plugin.getConfig().getBoolean("thor.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("thor.material", "STICK"),
                    messageManager.translateColors(plugin.getConfig().getString("thor.name", "thor")),
                    Keys.THOR,
                    plugin.getConfig().getInt("thor.usages", 10),
                    plugin.getConfig().getInt("thor.price", 10),
                    plugin.getConfig().getStringList("thor.lore"),
                    plugin.getConfig().getInt("thor.model-data", 0)));
        }
        if (plugin.getConfig().getBoolean("tank.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("tank.material", "DIAMOND_CHESTPLATE"),
                    messageManager.translateColors(plugin.getConfig().getString("tank.name", "tank")),
                    Keys.TANK,
                    plugin.getConfig().getInt("tank.usages", 5),
                    plugin.getConfig().getInt("tank.price", 10),
                    plugin.getConfig().getStringList("tank.lore"),
                    plugin.getConfig().getInt("tank.model-data", 0)));
        }
        if (plugin.getConfig().getBoolean("vampire.shop", true)) {
            perks.add(createPerk(
                    plugin.getConfig().getString("vampire.material", "REDSTONE"),
                    messageManager.translateColors(plugin.getConfig().getString("vampire.name", "vampire")),
                    Keys.VAMPIRE,
                    plugin.getConfig().getInt("vampire.usages", 5),
                    plugin.getConfig().getInt("vampire.price", 10),
                    plugin.getConfig().getStringList("vampire.lore"),
                    plugin.getConfig().getInt("vampire.model-data", 0)));
        }
        return perks;
    }
    public boolean doYouHaveThisPerk(ItemStack item,Player player){
        if (item == null || !item.hasItemMeta()) return false;
        List<ItemStack> perks = perkDataManager.getPlayerPerks(player.getUniqueId());
        for(ItemStack i:perks){

            if (i.getItemMeta().getPersistentDataContainer().getKeys().equals(item.getItemMeta().getPersistentDataContainer().getKeys())) {

                return true;
            }


        }



        return false;
    }
public void doAllChecksForPurchaseAndGivePlayerPerk(Integer gemsNeedIt, Player player, ItemStack itemStack, String perm){
        int gems =perkDataManager.getGems(player.getUniqueId());
        if (!player.hasPermission(perm)) {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1,1);
            String message = messageManager.getMessage("dont-have-perm-message");
            if(message!=null){
                player.sendMessage(message);
            }
        return;

        }
        if(gems>=gemsNeedIt){
            if(!doYouHaveThisPerk(itemStack,player)) {
                perkDataManager.setGems(player.getUniqueId(), perkDataManager.getGems(player.getUniqueId()) - gemsNeedIt);
                List<ItemStack> list = perkDataManager.getPlayerPerks(player.getUniqueId());
                ItemMeta meta = itemStack.getItemMeta();
                List<String> lore =meta.getLore();
                if (lore != null && lore.size() >= 2) {
                    lore.remove(lore.size() - 1);
                    lore.remove(lore.size() - 1);
                    lore.remove(lore.size() - 1);
                    lore.remove(lore.size() - 1);
                }
                meta.setLore(lore);
                itemStack.setItemMeta(meta);


                list.add(itemStack);
                perkDataManager.setPlayerPerks(player.getUniqueId(), list);

                String message = messageManager.getMessage("buy-perk-message",Map.of("perk",itemStack.getItemMeta().getDisplayName()));
                if(message!=null){
                    player.sendMessage(message);
                }

                player.closeInventory();
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);

                return;

            }
            String message = messageManager.getMessage("same-perk-message",Map.of("perk",itemStack.getItemMeta().getDisplayName()));
            if(message!=null){
                player.sendMessage(message);
            }
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1,1);

            return;
        }



        String message = messageManager.getMessage("not-enough-gems-message");
        if(message!=null){
        player.sendMessage(message);
        }
        player.playSound(player, Sound.ENTITY_VILLAGER_NO,1,1);
    }




    public void setPerk(ItemStack itemStack, Player player, Inventory inventory) {

        ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());

        if (currentPerk == null) {
            currentPerk = openPerkMenuCommand.createItemWithNameAndPersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONE_PERK);
            perkDataManager.setCurrentPerk(player.getUniqueId(), currentPerk);
        }


        if (currentPerk.equals(itemStack)) {

            perkDataManager.setCurrentPerk(player.getUniqueId(), openPerkMenuCommand.createItemWithNameAndPersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONE_PERK));
            player.getInventory().remove(itemStack);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS,1,1);
        } else {

            if(isInventoryFull(player)){

                String message = messageManager.getMessage("full-inv-message");
                if(message!=null){
                    player.sendMessage(message);
                }
                player.playSound(player, Sound.ENTITY_VILLAGER_NO,1,1);
                player.closeInventory();
                return;
            }


            perkDataManager.setCurrentPerk(player.getUniqueId(), itemStack);
            setPerkOnSlot(player,itemStack);
            String message = messageManager.getMessage("perks-chose-message",Map.of("perk",itemStack.getItemMeta().getDisplayName()));
            if(message!=null){
                player.sendMessage(message);
            }
            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()), itemStack);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1.5F);
        }


        openPerkMenuCommand.addItemsToInventoryMenu(inventory, perkDataManager.getPlayerPerks(player.getUniqueId()), 10, player);
    }
public void changeSlot(Player player){
    Inventory playerInv = player.getInventory();
    ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());
    if (currentPerk != null && currentPerk.getItemMeta() != null && !currentPerk.getItemMeta().getPersistentDataContainer().has(Keys.NONE_PERK)){

        playerInv.remove(currentPerk);



        setPerkOnSlot(player,currentPerk);

    }




}
public void setPerkOnSlot(Player player,ItemStack itemStack){
    Inventory playerInv = player.getInventory();
    ItemStack itemstack =playerInv.getItem(perkDataManager.getPerkSlot(player.getUniqueId()));
    if(itemstack!=null&&!openPerkMenuCommand.isPerkItem(itemstack)){



        new BukkitRunnable() {
            @Override
            public void run() {
                playerInv.addItem(itemstack);
            }
        }.runTaskLater(plugin,5);



    }


    playerInv.setItem(perkDataManager.getPerkSlot(player.getUniqueId()),itemStack);




}
    public boolean isInventoryFull(Player player) {
        Inventory inventory = player.getInventory();
        for (int i=0;i<36;i++) {
            ItemStack item =inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR|| openPerkMenuCommand.isPerkItem(item)) {
                return false;
            }
        }
        return true;
    }


}
