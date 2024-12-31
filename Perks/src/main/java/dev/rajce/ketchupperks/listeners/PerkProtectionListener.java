package dev.rajce.ketchupperks.listeners;

import dev.rajce.ketchupperks.commands.OpenPerkMenuCommand;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
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
    public  void  onPlayerPlaceBlock(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();

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


        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DECORATED_POT) {


            if (openPerkMenuCommand.isPerkItem(item)) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onArmorStandInteract(PlayerInteractAtEntityEvent event) {

        if (event.getRightClicked() instanceof ArmorStand) {
            ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
            if (openPerkMenuCommand.isPerkItem(itemInHand)) {
                event.setCancelled(true);

            }
        }
    }












}
