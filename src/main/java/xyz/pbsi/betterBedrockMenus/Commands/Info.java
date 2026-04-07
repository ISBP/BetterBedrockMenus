package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

import java.util.List;

public class Info implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length > 0 && args[0].equals("reload"))
        {
            if(!sender.hasPermission("bbm.admin"))
            {
                sender.sendMessage("§cNo permission!");
                return true;
            }
            BetterBedrockMenus.getInstance().reloadConfig();
            sender.sendMessage("§aReloaded the config!");

        }else {
            String version = BetterBedrockMenus.getInstance().getPluginMeta().getVersion();
            if(version.contains("beta"))
            {
                sender.sendMessage("§fBetter §aBedrock §fMenus §8(§7Beta / Internal§8)\n§8 | §fDeveloper: §apbsi\n§8 | §fVersion: §a"+version+"\n§8 | §fAPI Ver: §a"+BetterBedrockMenus.getInstance().getPluginMeta().getAPIVersion());

            }
            else {
                sender.sendMessage("§fBetter §aBedrock §fMenus §8(§7Prod§8)\n§8 | §fDeveloper: §apbsi\n§8 | §fVersion: §a"+version+"\n§8 | §fAPI Ver: §a"+BetterBedrockMenus.getInstance().getPluginMeta().getAPIVersion());
            }
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
