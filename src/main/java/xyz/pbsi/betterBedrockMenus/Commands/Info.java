package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.util.List;

public class Info implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length > 0 && args[0].equals("reload"))
        {
            BetterBedrockMenus.getInstance().reloadConfig();
            sender.sendMessage("§aReloaded the config!");
        }else {
            sender.sendMessage("§bBetter Bedrock Menus\n§bCreated by: §3pbsi");
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(args.length == 1)
        {
            return List.of("reload");
        }
        return null;
    }
}
