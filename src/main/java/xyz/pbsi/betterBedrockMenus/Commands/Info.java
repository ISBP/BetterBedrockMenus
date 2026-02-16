package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

public class Info implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        sender.sendMessage("§bBetter Bedrock Menus\n§bCreated by: §3pbsi");
        if(args.length > 0 && args[0].equals("reload"))
        {
            BetterBedrockMenus.getInstance().reloadConfig();
            sender.sendMessage("§aReloaded the config!");
        }
        return true;
    }
}
