package xyz.pbsi.betterBedrockMenus.Commands;

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
import java.util.HashMap;
import java.util.List;

public class MenuPremadeSender implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
            if(args.length < 2)
        {
            return false;
        }
        String fileName = args[0];
        Player preTargetPlayerJava = Bukkit.getPlayerExact(args[1]);

        boolean consoleCommand = false;
        Json json = new Json();
        if(args[0].charAt(0) == '-' && sender.hasPermission("bbm.console")) {
            if (args[0].equals("-c")) {
                consoleCommand = true;
                fileName = args[1];
                preTargetPlayerJava = Bukkit.getPlayerExact(args[2]);
            }
        }


        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");

        File file = new File(folder + "/" + fileName + ".json");


        if(!file.exists())
        {
            sender.sendMessage("§cThat menu does not exist!");
        }
        Player targetPlayerJava = preTargetPlayerJava;
        if(targetPlayerJava != null && FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId()) != null) {
            TextFormatter textFormatter = new TextFormatter();
            FloodgatePlayer targetPlayer = FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId());
            try {
                HashMap<String, String> hashMap = json.jsonToHashMap(file);
                String title = textFormatter.formatColorCodes(textFormatter.formatPlaceholders(hashMap.get("Title").replace("{player}", targetPlayer.getCorrectUsername()), targetPlayerJava));
                String body = textFormatter.formatColorCodes(textFormatter.formatPlaceholders(hashMap.get("Body").replace("{player}", targetPlayer.getCorrectUsername()), targetPlayerJava));
                String firstAction = hashMap.get("First Button Action");
                String secondAction = hashMap.get("Second Button Action");
                if(hashMap.get("Second Button Name") != null && !(hashMap.get("Second Button Name").isEmpty()))
                {
                    boolean finalConsoleCommand = consoleCommand;
                    SimpleForm.Builder form = formBuilder(title, body)
                            .button(hashMap.get("First Button Name"))
                            .button(hashMap.get("Second Button Name"))
                            .button("Close")
                            .validResultHandler(response -> {
                                        if(response.clickedButtonId() == 0)
                                        {
                                            if(firstAction.charAt(0) == '/')
                                            {
                                                if(finalConsoleCommand)
                                                {
                                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), textFormatter.formatPlaceholders(hashMap.get("First Button Action").replace("/", ""), targetPlayerJava));
                                                }
                                                else
                                                {
                                                    targetPlayerJava.performCommand(textFormatter.formatPlaceholders(hashMap.get("First Button Action").replace("/", ""), targetPlayerJava));
                                                }
                                            }
                                            else {
                                                targetPlayerJava.sendMessage(textFormatter.formatPlaceholders(firstAction, targetPlayerJava));
                                            }

                                        }
                                        if(response.clickedButtonId() == 1)
                                        {
                                            if(secondAction.charAt(0) == '/')
                                            {
                                                if(finalConsoleCommand)
                                                {
                                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), textFormatter.formatPlaceholders(hashMap.get("Second Button Action").replace("/", ""), targetPlayerJava));
                                                }
                                                else
                                                {
                                                    targetPlayerJava.performCommand(textFormatter.formatPlaceholders(hashMap.get("Second Button Action").replace("/", ""), targetPlayerJava));
                                                }                                            }
                                            else {
                                                targetPlayerJava.sendMessage(secondAction);
                                            }

                                        }
                                    }

                            );

                    targetPlayer.sendForm(form);
                }
                else if(hashMap.get("First Button Name") != null && !(hashMap.get("First Button Name").isEmpty()))
                {
                    boolean finalConsoleCommand = consoleCommand;
                    SimpleForm.Builder form = formBuilder(title, body)
                            .button(hashMap.get("First Button Name"))
                            .button("Close")
                            .validResultHandler(response -> {
                                        if(response.clickedButtonId() == 0)
                                        {
                                            if(firstAction.charAt(0) == '/')
                                            {
                                                if(finalConsoleCommand)
                                                {
                                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), textFormatter.formatPlaceholders(hashMap.get("First Button Action").replace("/", ""), targetPlayerJava));
                                                }
                                                else
                                                {
                                                    targetPlayerJava.performCommand(textFormatter.formatPlaceholders(hashMap.get("First Button Action").replace("/", ""), targetPlayerJava));
                                                }                                            }
                                            else {
                                                targetPlayerJava.sendMessage(textFormatter.formatPlaceholders(firstAction, targetPlayerJava));
                                            }

                                        }
                                    }

                            );

                    targetPlayer.sendForm(form);
                }
                else {


                    SimpleForm.Builder form = formBuilder(title, body)
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
        if(args.length == 1 || (args.length == 2 && args[0].equals("-c")))
        {
            return menus.getListOfMenus();
        }
        return null;
    }
    private SimpleForm.Builder formBuilder(String title, String body)
    {
        return SimpleForm.builder()
                .title(title)
                .content(body);
    }

}
