package dev.rajce.ketchupperks.listeners;

import dev.rajce.ketchupperks.commands.OpenPerkMenuCommand;
import dev.rajce.ketchupperks.Keys;
import dev.rajce.ketchupperks.PerkDataManager;
import dev.rajce.ketchupperks.Perks;
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

        if(perk!=null&&!perk.equals(openPerkMenuCommand.createItemWithNameAndPersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONE_PERK))){

            event.getDrops().remove(perk);
        }



        if (killer == null) return;
        perkDataManager.setGems(killer.getUniqueId(),perkDataManager.getGems(killer.getUniqueId())+plugin.getConfig().getInt("gems-per-kill",1));






    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();


        ItemStack perk = perkDataManager.getCurrentPerk(player.getUniqueId());

        if(perk!=null&&!perk.equals(openPerkMenuCommand.createItemWithNameAndPersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONE_PERK))){

            player.getInventory().setItem(perkDataManager.getPerkSlot(player.getUniqueId()),perk);
        }
    }
}
