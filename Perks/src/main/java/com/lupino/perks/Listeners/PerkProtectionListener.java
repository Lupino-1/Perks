package com.lupino.perks.Listeners;

import com.lupino.perks.Commands.OpenPerkMenuCommand;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class PerkProtectionListener implements Listener {


    private final OpenPerkMenuCommand openPerkMenuCommand;

    public PerkProtectionListener(OpenPerkMenuCommand openPerkMenuCommand){
        this.openPerkMenuCommand=openPerkMenuCommand;
    }





    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemStack mainHandItem = event.getOffHandItem();


        if (mainHandItem!=null&&openPerkMenuCommand.isPerkItem(mainHandItem)) {

            event.setCancelled(true);

        }
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if (openPerkMenuCommand.isPerkItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFramePlace(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame ) {

            ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();

            if (openPerkMenuCommand.isPerkItem(itemInHand)) {
                event.setCancelled(true);

            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player =event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item!=null&&openPerkMenuCommand.isPerkItem(item)&&isArmor(item.getType())) {

            event.setCancelled(true);

        }

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DECORATED_POT) {


            if (openPerkMenuCommand.isPerkItem(item)) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onArmorStandInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand ) {

            ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();

            if (openPerkMenuCommand.isPerkItem(itemInHand)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isArmor(Material material) {
        return material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE ||
                material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS ||
                material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE ||
                material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS ||
                material == Material.GOLDEN_HELMET || material == Material.GOLDEN_CHESTPLATE ||
                material == Material.GOLDEN_LEGGINGS || material == Material.GOLDEN_BOOTS ||
                material == Material.NETHERITE_HELMET || material == Material.NETHERITE_CHESTPLATE ||
                material == Material.NETHERITE_LEGGINGS || material == Material.NETHERITE_BOOTS ||
                material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE ||
                material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS ||
                material == Material.CHAINMAIL_HELMET || material == Material.CHAINMAIL_CHESTPLATE ||
                material == Material.CHAINMAIL_LEGGINGS || material == Material.CHAINMAIL_BOOTS;
    }








}
