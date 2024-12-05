package com.lupino.perks.Listeners;

import com.lupino.perks.*;
import com.lupino.perks.Commands.OpenPerkMenuCommand;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

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
        Inventory inventory = event.getInventory();
        ItemStack cursor = event.getCursor();
        int hotbarKey = event.getHotbarButton();

     
        if (hotbarKey != -1) {
            ItemStack hotbarItem = player.getInventory().getItem(hotbarKey);

            // Pokud je předmět v hotbaru nebo na slotu perk
            if (openPerkMenuCommand.isPerkItem(hotbarItem) || openPerkMenuCommand.isPerkItem(hand)) {
                event.setCancelled(true); 


            }
        }



        if (hand!= null && openPerkMenuCommand.isPerkItem(hand)&& perkDataManager.getCurrentPerk(player.getUniqueId())!=null&&!perkDataManager.getCurrentPerk(player.getUniqueId()).equals(openPerkMenuCommand.CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK))) {

            event.setCancelled(true);
            player.getInventory().remove(hand);
            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()),perkDataManager.getCurrentPerk(player.getUniqueId()));

        }
        if (cursor!= null && openPerkMenuCommand.isPerkItem(cursor)&&!perkDataManager.getCurrentPerk(player.getUniqueId()).equals(openPerkMenuCommand.CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK))) {

            event.setCancelled(true);
            player.getInventory().remove(cursor);
            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()),perkDataManager.getCurrentPerk(player.getUniqueId()));

        }
        if (event.getClick().isKeyboardClick() && openPerkMenuCommand.isPerkItem(cursor)||openPerkMenuCommand.isPerkItem(hand)) {

            event.setCancelled(true);


        }



        if(event.getView().getTitle().equals(ChatColor.BLACK+"Perks")){

            if (hand != null && hand.hasItemMeta() && openPerkMenuCommand.isPerkItem(hand)){
                SetPerk(hand,player,inventory);
            }



            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.PERKSHOPBUTTON)) {
                Inventory perkshop = Bukkit.createInventory(player, 54, ChatColor.BLACK+"Perks shop");
                openPerkMenuCommand.putEdgeItems(perkshop);
                List<ItemStack> listofallperks = CreateListofallPerks();
                openPerkMenuCommand.addItemsToInventory(perkshop,listofallperks,10);
                openPerkMenuCommand.setNameAndGivetoinvWithPersi(Material.BARRIER,ChatColor.RED+"Back",49,perkshop,Keys.BACK);
                openPerkMenuCommand.setNameAndGivetoinv(Material.SUNFLOWER,ChatColor.DARK_RED+"Your points: "+perkDataManager.getPoints(player.getUniqueId()),31,perkshop);

                player.openInventory(perkshop);


            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.SLOT)){
                Inventory slot = Bukkit.createInventory(player, 9, ChatColor.BLACK+"Inventory slot");
                openPerkMenuCommand.setNameAndGivetoinv(Material.GLASS_PANE,ChatColor.GREEN+"Tvůj slot",perkDataManager.getPerkSlot(player.getUniqueId()),slot);

                player.openInventory(slot);




            }

            event.setCancelled(true);
        }




        if(event.getView().getTitle().equals(ChatColor.BLACK+"Perks shop")){
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.SCOUT)) {
                doAllcheckAndgiveplperk(plugin.getConfig().getInt("Scout.Price", 10), player, event.getCurrentItem(),event.getInventory(),"perks.scout");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.JUMPER)) {
                doAllcheckAndgiveplperk(plugin.getConfig().getInt("Jumper.Price", 10), player, event.getCurrentItem(),event.getInventory(),"perks.jumper");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.RUNNER)) {
                doAllcheckAndgiveplperk(plugin.getConfig().getInt("Runner.Price", 10), player, event.getCurrentItem(),event.getInventory(),"perks.runner");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.MINER)) {
                doAllcheckAndgiveplperk(plugin.getConfig().getInt("Miner.Price", 10), player, event.getCurrentItem(),event.getInventory(),"perks.miner");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.KNIGHT)) {
                doAllcheckAndgiveplperk(plugin.getConfig().getInt("Knight.Price", 10), player, event.getCurrentItem(),event.getInventory(),"perks.knight");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.THOR)) {
                doAllcheckAndgiveplperk(plugin.getConfig().getInt("Thor.Price", 10), player, event.getCurrentItem(),event.getInventory(),"perks.thor");
            }
            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.TANK)) {
                doAllcheckAndgiveplperk(plugin.getConfig().getInt("Tank.Price", 10), player, event.getCurrentItem(),event.getInventory(),"perks.tank");
            }


            if (hand != null && hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.BACK)){

                openPerkMenuCommand.OpenMenu(player);
            }





        event.setCancelled(true);
        }







        if(event.getView().getTitle().equals(ChatColor.BLACK+"Inventory slot")){




            perkDataManager.setPerkSlot(player.getUniqueId(),event.getSlot());
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
            String message = messageManager.getMessage("SlotMessage",Map.of("slot",String.valueOf(perkDataManager.getPerkSlot(player.getUniqueId())+1)));
            if(message!=null){
                player.sendMessage(message);
            }
            changeSlot(player);



            event.setCancelled(true);
            player.closeInventory();
        }





    }








public ItemStack CreatePerk(Material material, String name, NamespacedKey namespacedKey,Integer numberofusages,Integer price, List<String> configLore){
    ItemStack itemStack = new ItemStack(material);
    ItemMeta meta = itemStack.getItemMeta();
    meta.setDisplayName(name);
    List<String> lore = new ArrayList<>();
    lore.add(ChatColor.WHITE + "Number of usages: " + numberofusages);


   
    if (configLore != null) {
        for (String line : configLore) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        lore.add("");
    }
    lore.add(ChatColor.GRAY + "Price: " + price + " points");
    meta.setLore(lore);
    meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER,numberofusages);
    meta.setUnbreakable(true);
    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    itemStack.setItemMeta(meta);
    return itemStack;
}
    public List<ItemStack> CreateListofallPerks( ) {
        List<ItemStack> perks = new ArrayList<>();
        if(plugin.getConfig().getBoolean("Scout.Shop",true)) {
            perks.add(CreatePerk(Material.FISHING_ROD, ChatColor.DARK_PURPLE + "Scout", Keys.SCOUT, plugin.getConfig().getInt("Scout.Usages", 5), plugin.getConfig().getInt("Scout.Price", 10),plugin.getConfig().getStringList("Scout.Lore")));
        }
        if(plugin.getConfig().getBoolean("Jumper.Shop",true)) {
            perks.add(CreatePerk(Material.FEATHER, ChatColor.DARK_PURPLE + "Jumper", Keys.JUMPER, plugin.getConfig().getInt("Jumper.Usages", 5), plugin.getConfig().getInt("Jumper.Price", 10),plugin.getConfig().getStringList("Jumper.Lore")));
        }
        if(plugin.getConfig().getBoolean("Runner.Shop",true)) {
            perks.add(CreatePerk(Material.SUGAR, ChatColor.DARK_PURPLE + "Runner", Keys.RUNNER, plugin.getConfig().getInt("Runner.Usages", 5), plugin.getConfig().getInt("Runner.Price", 10),plugin.getConfig().getStringList("Runner.Lore")));
        }
        if(plugin.getConfig().getBoolean("Miner.Shop",true)) {
            perks.add(CreatePerk(Material.IRON_PICKAXE, ChatColor.DARK_PURPLE + "Miner", Keys.MINER, plugin.getConfig().getInt("Miner.Usages", 10), plugin.getConfig().getInt("Miner.Price", 10),plugin.getConfig().getStringList("Miner.Lore")));
        }
        if(plugin.getConfig().getBoolean("Knight.Shop",true)) {
            perks.add(CreatePerk(Material.IRON_CHESTPLATE, ChatColor.DARK_PURPLE + "Knight", Keys.KNIGHT, plugin.getConfig().getInt("Knight.Usages", 5), plugin.getConfig().getInt("Knight.Price", 10),plugin.getConfig().getStringList("Knight.Lore")));
        }
        if(plugin.getConfig().getBoolean("Thor.Shop",true)) {
            perks.add(CreatePerk(Material.STICK, ChatColor.DARK_PURPLE + "Thor", Keys.THOR, plugin.getConfig().getInt("Thor.Usages", 10), plugin.getConfig().getInt("Thor.Price", 10),plugin.getConfig().getStringList("Thor.Lore")));
        }
        if(plugin.getConfig().getBoolean("Tank.Shop",true)) {
            perks.add(CreatePerk(Material.DIAMOND_CHESTPLATE, ChatColor.DARK_PURPLE + "Tank", Keys.TANK, plugin.getConfig().getInt("Tank.Usages", 5), plugin.getConfig().getInt("Tank.Price", 10),plugin.getConfig().getStringList("Tank.Lore")));
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
public void doAllcheckAndgiveplperk (Integer pointsneedit,Player player,ItemStack itemStack,Inventory inventory,String perm){
        int points =perkDataManager.getPoints(player.getUniqueId());
        if (!player.hasPermission(perm)) {
            String message = messageManager.getMessage("DontHavePermMessage");
            if(message!=null){
                player.sendMessage(message);
            }
        return;

        }
        if(points>=pointsneedit){
            if(!doYouHaveThisPerk(itemStack,player)) {
                perkDataManager.setPoints(player.getUniqueId(), perkDataManager.getPoints(player.getUniqueId()) - pointsneedit);
                List<ItemStack> list = perkDataManager.getPlayerPerks(player.getUniqueId());
                ItemMeta meta = itemStack.getItemMeta();
                List<String> lore =meta.getLore();
                if (lore != null && lore.size() >= 2) {
                    lore.remove(lore.size() - 1);
                    lore.remove(lore.size() - 1);
                }
                meta.setLore(lore);
                itemStack.setItemMeta(meta);


                list.add(itemStack);
                perkDataManager.setPlayerPerks(player.getUniqueId(), list);

                String message = messageManager.getMessage("BuyPerkMessage",Map.of("perk",itemStack.getItemMeta().getDisplayName()));
                if(message!=null){
                    player.sendMessage(message);
                }
                openPerkMenuCommand.setNameAndGivetoinv(Material.SUNFLOWER,ChatColor.DARK_RED+"Your points: "+perkDataManager.getPoints(player.getUniqueId()),31,inventory);
                player.closeInventory();
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);

                return;

            }
            String message = messageManager.getMessage("SamePerkMessage",Map.of("perk",itemStack.getItemMeta().getDisplayName()));
            if(message!=null){
                player.sendMessage(message);
            }
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1,1);

            return;
        }



        String message = messageManager.getMessage("NotEnoughPointsMessage");
        if(message!=null){
        player.sendMessage(message);
        }
        player.playSound(player, Sound.ENTITY_VILLAGER_NO,1,1);
    }




    public void SetPerk(ItemStack itemStack, Player player, Inventory inventory) {

        ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());

        if (currentPerk == null) {
            currentPerk = openPerkMenuCommand.CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK);
            perkDataManager.setCurrentPerk(player.getUniqueId(), currentPerk);
        }


        if (currentPerk.equals(itemStack)) {

            perkDataManager.setCurrentPerk(player.getUniqueId(), openPerkMenuCommand.CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK));
            player.getInventory().remove(itemStack);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS,1,1);
        } else {

            if(isInventoryFull(player)){

                String message = messageManager.getMessage("FullInvMessage");
                if(message!=null){
                    player.sendMessage(message);
                }
                player.playSound(player, Sound.ENTITY_VILLAGER_NO,1,1);
                player.closeInventory();
                return;
            }


            perkDataManager.setCurrentPerk(player.getUniqueId(), itemStack);
            setPerkOnSlot(player,itemStack);
            String message = messageManager.getMessage("PerksChoseMessage",Map.of("perk",itemStack.getItemMeta().getDisplayName()));
            if(message!=null){
                player.sendMessage(message);
            }
            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()), itemStack);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1.5F);
        }


        openPerkMenuCommand.addItemsToInventoryMenu(inventory, perkDataManager.getPlayerPerks(player.getUniqueId()), 10, player);
    }
public void changeSlot(Player player){
    Inventory plinv = player.getInventory();
    ItemStack currentPerk = perkDataManager.getCurrentPerk(player.getUniqueId());
    if (currentPerk != null && currentPerk.getItemMeta() != null && !currentPerk.getItemMeta().getPersistentDataContainer().has(Keys.NONEPERK)){

        plinv.remove(currentPerk);



        setPerkOnSlot(player,currentPerk);

    }




}
public void setPerkOnSlot(Player player,ItemStack itemStack){
    Inventory plinv = player.getInventory();
    ItemStack is =plinv.getItem(perkDataManager.getPerkSlot(player.getUniqueId()));
    if(is!=null&&!openPerkMenuCommand.isPerkItem(is)){



        new BukkitRunnable() {
            @Override
            public void run() {
                plinv.addItem(is);
            }
        }.runTaskLater(plugin,5);



    }


    plinv.setItem(perkDataManager.getPerkSlot(player.getUniqueId()),itemStack);




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
