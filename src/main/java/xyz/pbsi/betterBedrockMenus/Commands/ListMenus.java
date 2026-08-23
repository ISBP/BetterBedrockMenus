package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

public class ListMenus implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        String title = "§fList of §aMenus§8:";
        StringBuilder listOfMenus = new StringBuilder(title);
        for (String menu : Menus.getListOfMenus())
        {
            listOfMenus.append("\n§8- §f").append(menu);
        }
        sender.sendMessage(listOfMenus.toString());
        return true;
    }
}
