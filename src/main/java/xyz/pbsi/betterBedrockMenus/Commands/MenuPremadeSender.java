package xyz.pbsi.betterBedrockMenus.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Json;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;
import xyz.pbsi.betterBedrockMenus.Utils.TextFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuPremadeSender implements CommandExecutor, TabCompleter {

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
            TextFormatter textFormatter = new TextFormatter();
            FloodgatePlayer targetPlayer = FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId());
            try {
                HashMap<String, String> hashMap = json.jsonToHashMap(file);
                String title = textFormatter.formatColorCodes(PlaceholderAPI.setPlaceholders(targetPlayerJava, hashMap.get("Title").replace("{player}", targetPlayer.getCorrectUsername())));
                String body = textFormatter.formatColorCodes(PlaceholderAPI.setPlaceholders(targetPlayerJava, hashMap.get("Body").replace("{player}", targetPlayer.getCorrectUsername())));
                String action = hashMap.get("Button Action");
                if(hashMap.get("Button Name") != null)
                {
                    SimpleForm.Builder form = SimpleForm.builder()
                            .title(title)
                            .content(body)
                            .button(hashMap.get("Button Name"))
                            .button("Close")
                            .validResultHandler(response -> {
                                        if(response.clickedButtonId() == 0)
                                        {
                                            if(action.charAt(0) == '/')
                                            {
                                                targetPlayerJava.performCommand(hashMap.get("Button Action").replace("/", ""));
                                            }
                                            else {
                                                targetPlayerJava.sendMessage(action);
                                            }

                                        }
                                    }

                            );
                    targetPlayer.sendForm(form);
                }
                else {


                    SimpleForm.Builder form = SimpleForm.builder()
                            .title(title)
                            .content(body)
                            .button("Close");
                    targetPlayer.sendForm(form);
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

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
