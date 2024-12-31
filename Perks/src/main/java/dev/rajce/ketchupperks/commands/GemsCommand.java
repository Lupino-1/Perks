package dev.rajce.ketchupperks.commands;

import dev.rajce.ketchupperks.MessageManager;
import dev.rajce.ketchupperks.PerkDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GemsCommand implements CommandExecutor {

    private final PerkDataManager perkDataManager;
    private final MessageManager messageManager;

    public GemsCommand(PerkDataManager perkDataManager, MessageManager messageManager) {
        this.perkDataManager = perkDataManager;
        this.messageManager=messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                int gems = perkDataManager.getGems(player.getUniqueId());
                String message = messageManager.getMessage("gems-me-message",Map.of("gems",String.valueOf(gems)));
                if (message!=null) {

                    player.sendMessage(message);
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can check their own gems.");
                return false;
            }
        } else if (args.length == 1) {

            String playerName = args[0];
            Player targetPlayer = org.bukkit.Bukkit.getPlayerExact(playerName);

            if (targetPlayer == null) {
                sender.sendMessage(ChatColor.RED + "Player " + playerName + " is not online.");
                return false;
            }

            int gems = perkDataManager.getGems(targetPlayer.getUniqueId());
            String message = messageManager.getMessage("gems-other-player-message",Map.of("gems",String.valueOf(gems),"playername",playerName));
            if (message!=null) {

                sender.sendMessage(message);
            }

            return true;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /gems [player]");
        return false;
    }
}
