package com.lupino.perks.Commands;

import com.lupino.perks.MessageManager;
import com.lupino.perks.PerkDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class SetPointsCommand implements CommandExecutor {

    private final PerkDataManager perkDataManager;
    private final MessageManager messageManager;

    public SetPointsCommand(PerkDataManager perkDataManager,MessageManager messageManager) {
        this.perkDataManager = perkDataManager;
        this.messageManager= messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setpoints <player> <points>");
            return false;
        }

        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayerExact(playerName);

        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player " + playerName + " is not online.");
            return false;
        }

        try {
            int points = Integer.parseInt(args[1]);
            if (points < 0) {
                sender.sendMessage(ChatColor.RED + "Points cannot be less than 0.");
                return false;
            }

            perkDataManager.setPoints(targetPlayer.getUniqueId(), points);
            String message = messageManager.getMessage("SetPointsMessage", Map.of("points",String.valueOf(points),"playername",playerName));
            if (message!=null) {

                sender.sendMessage(message);
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Points must be a valid number.");
        }

        return true;
    }
}
