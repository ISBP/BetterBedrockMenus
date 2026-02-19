package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class MenuPremadeSender implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length != 2)
        {
            return false;
        }
        Json json = new Json();

        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");
        File file = new File(folder + "/" + args[0] + ".json");
        Player targetPlayerJava = Bukkit.getPlayerExact(args[1]);


        if(!file.exists())
        {
            sender.sendMessage("§cThat menu does not exist!");
        }
        if(targetPlayerJava != null && FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId()) != null) {
            FloodgatePlayer targetPlayer = FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId());
            try {
                HashMap<String, String> hashMap = json.jsonToHashMap(file);
                String title = hashMap.get("Title").replace("{player}", targetPlayer.getCorrectUsername());
                String body = hashMap.get("Body").replace("{player}", targetPlayer.getCorrectUsername());
                SimpleForm.Builder form = SimpleForm.builder()
                        .title(title)
                        .content(body)
                        .button("Close");
                targetPlayer.sendForm(form);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
