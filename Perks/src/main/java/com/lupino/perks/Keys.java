package com.lupino.perks;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.N;

import javax.print.attribute.standard.MediaSize;

public class Keys {

public static NamespacedKey PERKSHOPBUTTON;
public static NamespacedKey SCOUT;
public static NamespacedKey JUMPER;
public static NamespacedKey RUNNER;
public static NamespacedKey MINER;
public static NamespacedKey KNIGHT;
public static NamespacedKey THOR;
public static NamespacedKey SLOT;
public static NamespacedKey NONEPERK;
public static NamespacedKey BACK;
public static NamespacedKey LIGHTNING;
public static  NamespacedKey TANK;



    public static void initialize(Plugin plugin) {
        PERKSHOPBUTTON = new NamespacedKey(plugin, "perkshopbutton");
        SCOUT = new NamespacedKey(plugin, "scout");
        JUMPER = new NamespacedKey(plugin, "jumper");
        RUNNER = new NamespacedKey(plugin, "runner");
        MINER = new NamespacedKey(plugin, "miner");
        KNIGHT = new NamespacedKey(plugin, "knight");
        THOR = new NamespacedKey(plugin, "thor");
        SLOT = new NamespacedKey(plugin,"slot");
        NONEPERK = new NamespacedKey(plugin,"noneperk");
        BACK = new  NamespacedKey(plugin,"back");
        LIGHTNING = new NamespacedKey(plugin,"lightning");
        TANK = new NamespacedKey(plugin,"tank");



    }
}
