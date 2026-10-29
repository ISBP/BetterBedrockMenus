package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.awt.*;
import java.util.List;

public class OpenMenu implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!BetterBedrockMenus.getInstance().getConfig().getBoolean("Self Menu Open"))
        {
            sender.sendMessage("§cThis feature is not enabled!");
            return true;
        }
        if (args.length != 1)
        {
            return false;
        }
        String permission = "bbm.selfopen."+ args[0];

        if(sender instanceof Player player && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
        {
            if(!player.hasPermission(permission))
            {
                player.sendMessage("§cYou must have permission§4 " + permission + "§c!");
                return true;
            }
            if(!Menus.getListOfMenus().contains(args[0]))
            {
                sender.sendMessage("§cThat menu does not exist!");
                return true;
            }
            Menus.sendMenu(Bukkit.getConsoleSender(), player, args[0], false);
        }
        else {
            sender.sendMessage("§cMenus cannot be sent to you!");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args[0].isEmpty())
        {
            return Menus.getListOfMenus();
        }
        return Menus.getListOfMenusContains(args[0]);
    }
}
