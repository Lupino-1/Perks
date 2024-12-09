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

public class SetGemsCommand implements CommandExecutor {

    private final PerkDataManager perkDataManager;
    private final MessageManager messageManager;

    public SetGemsCommand(PerkDataManager perkDataManager, MessageManager messageManager) {
        this.perkDataManager = perkDataManager;
        this.messageManager= messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setgems <player> <points>");
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
                sender.sendMessage(ChatColor.RED + "Gems cannot be less than 0.");
                return false;
            }

            perkDataManager.setGems(targetPlayer.getUniqueId(), points);
            String message = messageManager.getMessage("YourGemsWereSetMessage", Map.of("gems",String.valueOf(points)));
            if (message!=null) {

                targetPlayer.sendMessage(message);
            }
            String message2 = messageManager.getMessage("SetGemsMessage", Map.of("gems",String.valueOf(points),"playername",playerName));
            if (message2!=null) {

                sender.sendMessage(message2);
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Gems must be a valid number.");
        }

        return true;
    }
}
