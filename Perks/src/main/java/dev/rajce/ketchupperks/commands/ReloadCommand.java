package dev.rajce.ketchupperks.commands;

import dev.rajce.ketchupperks.Perks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final Perks plugin;

    public ReloadCommand(Perks plugin){
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.RED + "Usage: /ketchupperks reload");
            return true;
        }

        if (!sender.hasPermission("ketchupperks.commands.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
            return true;
        }

        try {
            plugin.getMessageManager().reloadMessages();
            plugin.getPerkDataManager().reloadPerks();
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "KetchupPerks successfully reloaded.");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while reloading config.yml.");
            e.printStackTrace();
        }

        return true;
    }


}
