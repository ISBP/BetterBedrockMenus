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

import java.util.List;

public class OpenMenu implements CommandExecutor, TabCompleter {
    Menus menus = new Menus();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1)
        {
            return false;
        }
        if(!BetterBedrockMenus.getInstance().getConfig().getBoolean("Self Menu Open"))
        {
            sender.sendMessage("§cThis feature is not enabled!");
            return true;
        }

        if(sender instanceof Player player && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
        {
            if(!menus.getListOfMenus().contains(args[0]))
            {
                sender.sendMessage("§cThat menu does not exist!");
                return true;
            }
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "send-menu "+args[0]+" "+player.getName());
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
            return menus.getListOfMenus();
        }
        return menus.getListOfMenusContains(args[0]);
    }
}
