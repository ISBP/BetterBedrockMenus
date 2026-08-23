package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.util.List;

public class MenuSender implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length < 2 || (args.length < 3 && args[0].contains("-c")))
        {
            return false;
        }
        String fileName = args[0];
        Player preTargetPlayerJava = Bukkit.getPlayerExact(args[1]);
        boolean consoleCommand;
        if(args[0].charAt(0) == '-' && sender.hasPermission("bbm.console")) {
            if (args[0].equals("-c")) {
                consoleCommand = true;
                fileName = args[1];
                preTargetPlayerJava = Bukkit.getPlayerExact(args[2]);
            } else {
                consoleCommand = false;
            }
        } else {
            consoleCommand = false;
        }
        Menus.sendMenu(sender, preTargetPlayerJava, fileName, consoleCommand);
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1 || (args.length == 2 && args[0].equals("-c")))
        {
            int argument = args.length-1;
            if(args[argument].isEmpty())
            {
                return Menus.getListOfMenus();
            }
            return Menus.getListOfMenusContains(args[argument]);

        }
        return null;
    }


}
