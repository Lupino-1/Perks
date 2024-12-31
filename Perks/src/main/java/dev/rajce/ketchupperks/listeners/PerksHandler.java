package dev.rajce.ketchupperks.listeners;


import dev.rajce.ketchupperks.Keys;
import dev.rajce.ketchupperks.MessageManager;
import dev.rajce.ketchupperks.PerkDataManager;
import dev.rajce.ketchupperks.Perks;
import dev.rajce.ketchupperks.commands.OpenPerkMenuCommand;
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

public class PerksHandler implements Listener {

    private  final PerkDataManager perkDataManager;
    private final OpenPerkMenuCommand openPerkMenuCommand;
    private final MessageManager messageManager;
    private final Perks plugin;


    private final HashMap<UUID, Long> scoutCooldown = new HashMap<>();
    private final HashMap<UUID, Long> jumperCooldown = new HashMap<>();
    private final HashMap<UUID, Long> runnerCooldown = new HashMap<>();
    private final HashMap<UUID, Long> minerCooldown = new HashMap<>();
    private final HashMap<UUID, Long> knightCooldown = new HashMap<>();
    private final HashMap<UUID, Long> thorCooldown = new HashMap<>();
    private final HashMap<UUID, Long> tankCooldown = new HashMap<>();
    private final HashMap<UUID,Long> vampireCooldown = new HashMap<>();


    public PerksHandler(PerkDataManager perkDataManager, OpenPerkMenuCommand openPerkMenuCommand, MessageManager messageManager, Perks plugin) {


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
        if (hand!=null&&openPerkMenuCommand.isPerkItem(hand)&&isArmor(hand.getType())) {

            event.setCancelled(true);

        }
        if(World.Environment.NETHER==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("enable-perks-in-nether",true))return;
        if(World.Environment.NORMAL==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("enable-perks-in-overworld",true))return;
        if(World.Environment.THE_END==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("enable-perks-in-end",true))return;
        if(openPerkMenuCommand.isInAnySafeZone(player)){
            if(hand.hasItemMeta() && openPerkMenuCommand.isPerkItem(hand)){
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        openPerkMenuCommand.openMenu(player);
                    }
                }.runTaskLater(plugin, 2 );


            }
            return;
        }




        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.JUMPER)){
            if(isOnCooldown(player, jumperCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player, jumperCooldown,plugin.getConfig().getInt("jumper.cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("jumper.cooldown",60));
            subractUsage(player,hand,Keys.JUMPER);
            Vector direction = player.getLocation().getDirection().normalize();
            player.setVelocity(direction.multiply(plugin.getConfig().getInt("jumper.power-of-push",2)));
        }


        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.RUNNER)){
            if(isOnCooldown(player, runnerCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player, runnerCooldown,plugin.getConfig().getInt("runner.cooldown",120));
            sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("runner.cooldown",120));

            subractUsage(player,hand,Keys.RUNNER);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,plugin.getConfig().getInt("runner.duration",10)*20,plugin.getConfig().getInt("runner.power-of-effect",4)-1));
        }



        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.MINER)){
            if(isOnCooldown(player, minerCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player, minerCooldown,plugin.getConfig().getInt("miner.cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("miner.cooldown",60));
            subractUsage(player,hand,Keys.MINER);
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,plugin.getConfig().getInt("miner.duration",30)*20,plugin.getConfig().getInt("miner.power-of-effect",3)-1));


        }



        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.KNIGHT)){
            if(isOnCooldown(player, knightCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player, knightCooldown,plugin.getConfig().getInt("knight.cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("knight.cooldown",60));
            subractUsage(player,hand,Keys.KNIGHT);
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH,plugin.getConfig().getInt("knight.duration",5)*20,plugin.getConfig().getInt("knight.power-of-effect",4)-1));

        }


        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.THOR)){
            if(isOnCooldown(player, thorCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            RayTraceResult result = player.rayTraceBlocks(150);
            if (result != null && result.getHitBlock() != null ) {
                Location location = result.getHitBlock().getLocation();
                LightningStrike lightning = location.getWorld().strikeLightning(location);
                lightning.getPersistentDataContainer().set(Keys.LIGHTNING,PersistentDataType.BOOLEAN,true);
                setCooldown(player, thorCooldown, plugin.getConfig().getInt("thor.cooldown",60));
                sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("thor.cooldown",60));
                subractUsage(player, hand, Keys.THOR);
            }
        }
        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.TANK)){
            if(isOnCooldown(player, tankCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player, tankCooldown,plugin.getConfig().getInt("tank.cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("tank.cooldown",60));
            subractUsage(player,hand,Keys.TANK);
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,plugin.getConfig().getInt("tank.duration",5)*20,plugin.getConfig().getInt("tank.power-of-effect",4)-1));

        }
        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.VAMPIRE)){
            if(isOnCooldown(player, vampireCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }
            setCooldown(player, vampireCooldown,plugin.getConfig().getInt("vampire.cooldown",60));
            sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("vampire.cooldown",60));
            subractUsage(player,hand,Keys.VAMPIRE);
            Location loc = player.getLocation();
            spawnParticleSphere(loc, plugin.getConfig().getInt("vampire.radius",7), Particle.DAMAGE_INDICATOR,15);
            loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_BREATH, 2, 2);

            for (Player targeted : Bukkit.getOnlinePlayers()) {
                if (targeted != player) {
                    if(openPerkMenuCommand.isInAnySafeZone(targeted)) return;
                    if (player.getLocation().distance(targeted.getLocation()) < plugin.getConfig().getInt("vampire.radius",7)) {
                        double newHealth = targeted.getHealth() - plugin.getConfig().getInt("vampire.damage-and-heal",2);
                        if (newHealth <= 0) {
                            targeted.damage(15);
                        } else {
                            targeted.setHealth(newHealth);
                        }
                        if (player.getHealth() < 20) {
                            player.setHealth(Math.min(player.getHealth() + plugin.getConfig().getInt("vampire.damage-and-heal",2), 20));
                        }

                    }
                }
            }
        }





    }
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if(World.Environment.NETHER==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("enable-perks-in-nether",true))return;
        if(World.Environment.NORMAL==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("enable-perks-in-overworld",true))return;
        if(World.Environment.THE_END==player.getWorld().getEnvironment()&& !plugin.getConfig().getBoolean("enable-perks-in-end",true))return;
        if(openPerkMenuCommand.isInAnySafeZone(player)){
            if(hand.hasItemMeta() && openPerkMenuCommand.isPerkItem(hand)){
                openPerkMenuCommand.openMenu(player);

            }
            event.setCancelled(true);
            return;
        }




        if (hand.hasItemMeta() && Objects.requireNonNull(hand.getItemMeta()).getPersistentDataContainer().has(Keys.SCOUT)){

            if(isOnCooldown(player, scoutCooldown,true,hand)){
                event.setCancelled(true);
                return;
            }



            Vector direction = player.getLocation().getDirection().normalize();
            hook.setVelocity(direction.multiply(2.5)); 

        Location hookLocation = event.getHook().getLocation();

        if (hook.isInWater() ||isBlockNearby(hookLocation,player)|| event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            if(isOnCooldown(player, scoutCooldown,true,hand)){
                return;
            }
            setCooldown(player, scoutCooldown,plugin.getConfig().getInt("scout.cooldown",60));

            sendMessageAfterSomeTime(player,messageManager.getMessage("after-cooldown-message",Map.of("perk",hand.getItemMeta().getDisplayName())),plugin.getConfig().getInt("scout.cooldown",60));
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
                event.setDamage(plugin.getConfig().getInt("thor.damage",30));

            }

        }
    }


    private double calculateDistance(Location playerLocation, Location hookLocation) {
        return playerLocation.distance(hookLocation);
    }
    private Vector calculatePullVector(Location playerLocation, Location hookLocation, double distance) {
        Vector direction = hookLocation.toVector().subtract(playerLocation.toVector()).normalize();


        double pushStrength = Math.min(1.0 + (distance / 7.0), plugin.getConfig().getDouble("jumper.power-of-push",2.5));
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

                String message = messageManager.getMessage("cooldown-message",Map.of("cooldown",String.valueOf(timeLeft),"perk",perk.getItemMeta().getDisplayName()));
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
            String message = messageManager.getMessage("last-usage-message",Map.of("perk",itemStack.getItemMeta().getDisplayName()));
            if(message!=null){
                player.sendMessage(message);
            }

            perkDataManager.setCurrentPerk(player.getUniqueId(),openPerkMenuCommand.createItemWithNameAndPersi(Material.GRAY_DYE, "Nevybraný perk", Keys.NONE_PERK));
            perkDataManager.removePerkFromList(player.getUniqueId(),itemStack);
            player.getInventory().remove(itemStack);

            return;
        }

        perkDataManager.removePerkFromList(player.getUniqueId(),itemStack);
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(namespacedKey,PersistentDataType.INTEGER, usages-1);

        usages-=1;


        String message = messageManager.getMessage("usages-message",Map.of("usagesleft",String.valueOf(usages),"perk",itemStack.getItemMeta().getDisplayName()));
        if(message!=null){
            player.sendMessage(message);
        }

        List<String> lore = meta.getLore();
        lore.remove(0);
        lore.add(0,ChatColor.WHITE+"Počet použití "+usages);


        meta.setLore(lore);

        itemStack.setItemMeta(meta);
        perkDataManager.addPerkToList(player.getUniqueId(),itemStack);
        perkDataManager.setCurrentPerk(player.getUniqueId(),itemStack);


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
    private void spawnParticleSphere(Location center, double radius, Particle particle,double density) {
        double step = Math.PI / density;


        for (double theta = 0; theta <= Math.PI; theta += step) {
            for (double phi = 0; phi <= 2 * Math.PI; phi += step) {
                double x = radius * Math.sin(theta) * Math.cos(phi);
                double y = radius * Math.cos(theta);
                double z = radius * Math.sin(theta) * Math.sin(phi);

                center.getWorld().spawnParticle(particle, center.clone().add(x, y, z), 1, 0, 0, 0, 0);
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

