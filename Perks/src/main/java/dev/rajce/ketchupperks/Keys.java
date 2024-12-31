package dev.rajce.ketchupperks;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class Keys {

public static NamespacedKey PERK_SHOP_BUTTON;
public static NamespacedKey SCOUT;
public static NamespacedKey JUMPER;
public static NamespacedKey RUNNER;
public static NamespacedKey MINER;
public static NamespacedKey KNIGHT;
public static NamespacedKey THOR;
public static NamespacedKey SLOT;
public static NamespacedKey NONE_PERK;
public static NamespacedKey BACK;
public static NamespacedKey LIGHTNING;
public static  NamespacedKey TANK;
public static NamespacedKey VAMPIRE;
public static NamespacedKey PERK_CHOOSE_BUTTON;
public static NamespacedKey CLOSE_BUTTON;




    public static void initialize(Plugin plugin) {
        PERK_SHOP_BUTTON = new NamespacedKey(plugin, "perk_shop_button");
        SCOUT = new NamespacedKey(plugin, "scout");
        JUMPER = new NamespacedKey(plugin, "jumper");
        RUNNER = new NamespacedKey(plugin, "runner");
        MINER = new NamespacedKey(plugin, "miner");
        KNIGHT = new NamespacedKey(plugin, "knight");
        THOR = new NamespacedKey(plugin, "thor");
        SLOT = new NamespacedKey(plugin,"slot");
        NONE_PERK = new NamespacedKey(plugin,"none_perk");
        BACK = new  NamespacedKey(plugin,"back");
        LIGHTNING = new NamespacedKey(plugin,"lightning");
        TANK = new NamespacedKey(plugin,"tank");
        VAMPIRE = new NamespacedKey(plugin,"vampire");
        PERK_CHOOSE_BUTTON = new NamespacedKey(plugin,"perk_choose_button");
        CLOSE_BUTTON = new NamespacedKey(plugin,"close_button");
    }
}
