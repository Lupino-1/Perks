package com.lupino.perks.Listeners;

import com.lupino.perks.Commands.OpenPerkMenuCommand;
import com.lupino.perks.Keys;
import com.lupino.perks.PerkDataManager;
import com.lupino.perks.Perks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerKillListener implements Listener {

    private  final PerkDataManager perkDataManager;
    private final OpenPerkMenuCommand openPerkMenuCommand;
    private final Perks plugin;

    public PlayerKillListener ( PerkDataManager perkDataManager,OpenPerkMenuCommand openPerkMenuCommand,Perks plugin) {


        this.perkDataManager=perkDataManager;
        this.openPerkMenuCommand=openPerkMenuCommand;
        this.plugin=plugin;
    }

@EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player killedPlayer = event.getEntity();
        Player killer = killedPlayer.getKiller();


        ItemStack perk = perkDataManager.getCurrentPerk(killedPlayer.getUniqueId());

        if(perk!=null&&!perk.equals(openPerkMenuCommand.CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK))){

            event.getDrops().remove(perk);
        }



        if (killer == null) return;
        perkDataManager.setGems(killer.getUniqueId(),perkDataManager.getGems(killer.getUniqueId())+plugin.getConfig().getInt("Gemsperkill",1));






    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();


        ItemStack perk = perkDataManager.getCurrentPerk(player.getUniqueId());

        if(perk!=null&&!perk.equals(openPerkMenuCommand.CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK))){

            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()),perk);
        }
    }
}
