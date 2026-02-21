package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuDeleter implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");

        if(args.length != 1)
        {
            return false;
        }

        File file = new File(folder + "/" + args[0]+".json");
        if(!file.delete())
        {

            if(file.exists())
            {
                //This occurs when a menu was recently sent to a player, and the file is in use.
                sender.sendMessage("§cThat file is in use! Please wait ~10 seconds before attempting to delete it again!");
            }else {
                sender.sendMessage("§cThat menu does not exist!");
            }
            return true;
        }
        sender.sendMessage("§aSuccessfully deleted " + args[0]);
        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Menus menus = new Menus();

        if(args.length == 1)
        {
            return menus.getListOfMenus();
        }
        return null;
    }
}
