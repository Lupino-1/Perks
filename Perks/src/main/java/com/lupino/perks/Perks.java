package com.lupino.perks;

import com.lupino.perks.Commands.AddGemsCommand;
import com.lupino.perks.Commands.OpenPerkMenuCommand;
import com.lupino.perks.Commands.GemsCommand;
import com.lupino.perks.Commands.SetGemsCommand;
import com.lupino.perks.Listeners.MenuListener;
import com.lupino.perks.Listeners.PerkProtectionListener;
import com.lupino.perks.Listeners.PlayerKillListener;
import com.lupino.perks.Listeners.WeaponsHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Perks extends JavaPlugin {


    private PerkDataManager perkDataManager;
    private  MessageManager messageManager;
    private OpenPerkMenuCommand openPerkMenuCommand;


    @Override
    public void onEnable() {


        Keys.initialize(this);
        saveDefaultConfig();

        perkDataManager = new PerkDataManager(this);
        messageManager= new MessageManager(this);
        openPerkMenuCommand = new OpenPerkMenuCommand(perkDataManager,this,messageManager);




        getCommand("perks").setExecutor(openPerkMenuCommand);
        getServer().getPluginManager().registerEvents(new MenuListener(perkDataManager,openPerkMenuCommand,messageManager,this),this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(perkDataManager,openPerkMenuCommand,this),this);
        getServer().getPluginManager().registerEvents(new PerkProtectionListener(openPerkMenuCommand),this);
        getServer().getPluginManager().registerEvents(new WeaponsHandler(perkDataManager,openPerkMenuCommand,messageManager,this),this);
        getCommand("setgems").setExecutor(new SetGemsCommand(perkDataManager,messageManager));
        getCommand("gems").setExecutor(new GemsCommand(perkDataManager,messageManager));
        getCommand("addgems").setExecutor(new AddGemsCommand(perkDataManager,messageManager));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
