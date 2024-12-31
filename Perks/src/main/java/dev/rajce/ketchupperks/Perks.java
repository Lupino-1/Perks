package dev.rajce.ketchupperks;

import dev.rajce.ketchupperks.commands.*;
import dev.rajce.ketchupperks.listeners.MenuListener;
import dev.rajce.ketchupperks.listeners.PerkProtectionListener;
import dev.rajce.ketchupperks.listeners.PlayerKillListener;
import dev.rajce.ketchupperks.listeners.PerksHandler;
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




        getCommand("perkmenu").setExecutor(openPerkMenuCommand);
        getServer().getPluginManager().registerEvents(new MenuListener(perkDataManager,openPerkMenuCommand,messageManager,this),this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(perkDataManager,openPerkMenuCommand,this),this);
        getServer().getPluginManager().registerEvents(new PerkProtectionListener(openPerkMenuCommand),this);
        getServer().getPluginManager().registerEvents(new PerksHandler(perkDataManager,openPerkMenuCommand,messageManager,this),this);
        getCommand("setgems").setExecutor(new SetGemsCommand(perkDataManager,messageManager));
        getCommand("gems").setExecutor(new GemsCommand(perkDataManager,messageManager));
        getCommand("addgems").setExecutor(new AddGemsCommand(perkDataManager,messageManager));
        getCommand("ketchupperks").setExecutor(new ReloadCommand(this));
    }

    public PerkDataManager getPerkDataManager() {
        return perkDataManager;
    }


    public MessageManager getMessageManager() {
        return messageManager;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
