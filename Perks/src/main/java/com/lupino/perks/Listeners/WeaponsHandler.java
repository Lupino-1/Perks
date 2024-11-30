package com.lupino.perks.Listeners;

import com.lupino.perks.*;
import com.lupino.perks.Commands.OpenPerkMenuCommand;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class WeaponsHandler implements Listener {

    private  final PerkDataManager perkDataManager;
    private final OpenPerkMenuCommand openPerkMenuCommand;
    private final MessageManager messageManager;
    private final Perks plugin;


    private final HashMap<UUID, Long> scoutcooldown = new HashMap<>();
    private final HashMap<UUID, Long> jumpercooldown = new HashMap<>();
    private final HashMap<UUID, Long> runnercooldown = new HashMap<>();
    private final HashMap<UUID, Long> minercooldown = new HashMap<>();
    private final HashMap<UUID, Long> knightcooldown = new HashMap<>();
    private final HashMap<UUID, Long> thorcooldown = new HashMap<>();

    private final HashMap<UUID, Long> tankcooldown = new HashMap<>();


    public WeaponsHandler ( PerkDataManager perkDataManager,OpenPerkMenuCommand openPerkMenuCommand,MessageManager messageManager,Perks plugin) {


        this.perkDataManager=perkDataManager;
        this.openPerkMenuCommand=openPerkMenuCommand;
        this.messageManager= messageManager;
        this.plugin=plugin;

    }

    @EventHandler
    public void  onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getHand()!= EquipmentSlot.HAND||event.getAction()== Action.LEFT_CLICK_AIR||event.getAction()==Action.LEFT_CLICK_BLOCK)return;
        ItemStack hand = player.getItemInHand();
        if(World.Environment.NETHER==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("EnablePerksInNether",true))return;
        if(World.Environment.NORMAL==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("EnablePerksInOverworld",true))return;
        if(World.Environment.THE_END==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("EnablePerksInEnd",true))return;
        if(isInAnySafeZone(player))return;

        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.JUMPER)){
            if(isOnCooldown(player,jumpercooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player,jumpercooldown,plugin.getConfig().getInt("Jumper.Cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("AfterCooldownMessage",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("Jumper.Cooldown",60));
            subractUsage(player,hand,Keys.JUMPER);
            Vector direction = player.getLocation().getDirection().normalize();
            player.setVelocity(direction.multiply(plugin.getConfig().getInt("Jumper.Powerofpush",2)));
        }


        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.RUNNER)){
            if(isOnCooldown(player,runnercooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player,runnercooldown,plugin.getConfig().getInt("Runner.Cooldown",120));
            sendMessageAfterSomeTime(player,messageManager.getMessage("AfterCooldownMessage",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("Runner.Cooldown",120));

            subractUsage(player,hand,Keys.RUNNER);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,plugin.getConfig().getInt("Runner.Duration",10)*20,plugin.getConfig().getInt("Runner.Powerofeffect",4)-1));
        }



        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.MINER)){
            if(isOnCooldown(player,minercooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player,minercooldown,plugin.getConfig().getInt("Miner.Cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("AfterCooldownMessage",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("Miner.Cooldown",60));
            subractUsage(player,hand,Keys.MINER);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,plugin.getConfig().getInt("Miner.Duration",30)*20,plugin.getConfig().getInt("Miner.Powerofeffect",3)-1));


        }



        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.KNIGHT)){
            if(isOnCooldown(player,knightcooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player,knightcooldown,plugin.getConfig().getInt("Knight.Cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("AfterCooldownMessage",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("Knight.Cooldown",60));
            subractUsage(player,hand,Keys.KNIGHT);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,plugin.getConfig().getInt("Knight.Duration",5)*20,plugin.getConfig().getInt("Knight.Powerofeffect",4)-1));

        }


        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.THOR)){
            if(isOnCooldown(player,thorcooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            RayTraceResult result = player.rayTraceBlocks(150);
            if (result != null && result.getHitBlock() != null ) {
                Location location = result.getHitBlock().getLocation();
                LightningStrike lightning = location.getWorld().strikeLightning(location);
                lightning.getPersistentDataContainer().set(Keys.LIGHTNING,PersistentDataType.BOOLEAN,true);
                setCooldown(player, thorcooldown, plugin.getConfig().getInt("Thor.Cooldown",60));
                sendMessageAfterSomeTime(player,messageManager.getMessage("AfterCooldownMessage",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("Thor.Cooldown",60));
                subractUsage(player, hand, Keys.THOR);
            }
        }
        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.TANK)){
            if(isOnCooldown(player,tankcooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player,tankcooldown,plugin.getConfig().getInt("Tank.Cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("AfterCooldownMessage",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("Tank.Cooldown",60));
            subractUsage(player,hand,Keys.TANK);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,plugin.getConfig().getInt("Tank.Duration",5)*20,plugin.getConfig().getInt("Tank.Powerofeffect",4)-1));

        }






    }
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if(World.Environment.NETHER==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("EnablePerksInNether",true))return;
        if(World.Environment.NORMAL==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("EnablePerksInOverworld",true))return;
        if(World.Environment.THE_END==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("EnablePerksInEnd",true))return;
        if(isInAnySafeZone(player))return;


        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.SCOUT)){

            if(isOnCooldown(player,scoutcooldown,true,hand)){
                event.setCancelled(true);
                return;
            }



            Vector direction = player.getLocation().getDirection().normalize();
            hook.setVelocity(direction.multiply(2.5)); // Nastavení větší síly

        Location hookLocation = event.getHook().getLocation();

        if (hook.isInWater() ||isBlockNearby(hookLocation,player)|| event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            if(isOnCooldown(player,scoutcooldown,true,hand)){
                return;
            }
            setCooldown(player,scoutcooldown,plugin.getConfig().getInt("Scout.Cooldown",60));

            sendMessageAfterSomeTime(player,messageManager.getMessage("AfterCooldownMessage",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("Scout.Cooldown",60));
            subractUsage(player,hand,Keys.SCOUT);

            double distance = calculateDistance(player.getLocation(), hookLocation);


            Vector pullVector = calculatePullVector(player.getLocation(), hookLocation, distance);


            player.setVelocity(pullVector);

                }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        if (damager instanceof LightningStrike) {
            LightningStrike lightning = (LightningStrike) damager;

            if (lightning.getPersistentDataContainer().has(Keys.LIGHTNING)) {
                event.setDamage(plugin.getConfig().getInt("Thor.Damage",30));

            }

        }
    }


    private double calculateDistance(Location playerLocation, Location hookLocation) {
        return playerLocation.distance(hookLocation);
    }
    private Vector calculatePullVector(Location playerLocation, Location hookLocation, double distance) {
        Vector direction = hookLocation.toVector().subtract(playerLocation.toVector()).normalize();


        double pushStrength = Math.min(1.0 + (distance / 7.0), 3.0);
        return direction.multiply(pushStrength);
    }
    public boolean isBlockNearby(Location center,Player player) {
        World world = center.getWorld();
        if (world == null) return false;


        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {




                    Location nearbyLoc = center.clone().add(x, y, z);
                    Material blockType = world.getBlockAt(nearbyLoc).getType();


                    if (blockType != Material.AIR&&blockType != Material.WATER&&player.getLocation().distance(center)>3) {
                        return true;
                    }
                }
            }
        }

        return false;
    }




    private void setCooldown(Player player, HashMap<UUID, Long> cooldowns, int seconds) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }
    private boolean isOnCooldown(Player player, HashMap<UUID, Long> cooldowns,boolean sendtimeleft,ItemStack perk) {
        UUID playerUUID = player.getUniqueId();
        if (cooldowns.containsKey(playerUUID)) {
            long timeLeft = (cooldowns.get(playerUUID) - System.currentTimeMillis()) / 1000;

            if(timeLeft>0&&sendtimeleft) {

                String message = messageManager.getMessage("CooldownMessage",Map.of("cooldown",String.valueOf(timeLeft),"perk",perk.getItemMeta().getDisplayName()));
                if(message!=null){
                    player.sendMessage(message);
                }
            }
            return timeLeft > 0;
        }
        return false;
    }

    private void subractUsage(Player player, ItemStack itemStack, NamespacedKey namespacedKey){
        int usages =itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);

        if(usages<=1){

            perkDataManager.setCurrentPerk(player.getUniqueId(),openPerkMenuCommand.CreateItemWithNameandpersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONEPERK));
            perkDataManager.removePerkFromList(player.getUniqueId(),itemStack);
            player.getInventory().remove(itemStack);

            return;
        }

        perkDataManager.removePerkFromList(player.getUniqueId(),itemStack);
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(namespacedKey,PersistentDataType.INTEGER, usages-1);

        usages-=1;


        String message = messageManager.getMessage("UsagesMessage",Map.of("usagesleft",String.valueOf(usages),"perk",itemStack.getItemMeta().getDisplayName()));
        if(message!=null){
            player.sendMessage(message);
        }

        List<String> lore = Arrays.asList(ChatColor.WHITE+"Number of usages "+usages);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        perkDataManager.addPerkToList(player.getUniqueId(),itemStack);
        perkDataManager.setCurrentPerk(player.getUniqueId(),itemStack);


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


    public boolean isInAnySafeZone (Player player){
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Overworld.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Overworld.loc2")))){
            return true;
        }
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Nether.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.Nether.loc2")))){
            return true;
        }
        if (isInSafeZone(player, Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.End.loc1")), Objects.requireNonNull(plugin.getConfig().getLocation("Safezone.End.loc2")))){
            return true;
        }


        return false;

    }
    public void sendMessageAfterSomeTime(Player player,String message,Integer seconds){
        new BukkitRunnable() {

            @Override
            public void run() {
                if (message!=null) {

                    player.sendMessage(message);
                }
            }
        }.runTaskLater(plugin, seconds*20 );


    }

}

