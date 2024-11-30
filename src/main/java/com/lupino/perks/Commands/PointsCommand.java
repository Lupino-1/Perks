package com.lupino.perks.Commands;

import com.lupino.perks.MessageManager;
import com.lupino.perks.PerkDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class PointsCommand implements CommandExecutor {

    private final PerkDataManager perkDataManager;
    private final MessageManager messageManager;

    public PointsCommand(PerkDataManager perkDataManager,MessageManager messageManager) {
        this.perkDataManager = perkDataManager;
        this.messageManager=messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                int points = perkDataManager.getPoints(player.getUniqueId());
                String message = messageManager.getMessage("PointsMeMessage",Map.of("points",String.valueOf(points)));
                if (message!=null) {

                    player.sendMessage(message);
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can check their own points.");
                return false;
            }
        } else if (args.length == 1) {

            String playerName = args[0];
            Player targetPlayer = org.bukkit.Bukkit.getPlayerExact(playerName);

            if (targetPlayer == null) {
                sender.sendMessage(ChatColor.RED + "Player " + playerName + " is not online.");
                return false;
            }

            int points = perkDataManager.getPoints(targetPlayer.getUniqueId());
            String message = messageManager.getMessage("PointsOtherPlayerMessage",Map.of("points",String.valueOf(points),"playername",playerName));
            if (message!=null) {

                sender.sendMessage(message);
            }

            return true;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /points [player]");
        return false;
    }
}
