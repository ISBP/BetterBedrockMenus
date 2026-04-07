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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuPremadeSender implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length < 2)
        {
            return false;
        }
        if(args.length < 3 && args[0].contains("-c"))
        {
            return false;
        }
        String fileName = args[0];
        Player preTargetPlayerJava = Bukkit.getPlayerExact(args[1]);

        boolean consoleCommand;
        Json json = new Json();
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


        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");

        File file = new File(folder + "/" + fileName + ".json");


        if(!file.exists())
        {
            sender.sendMessage("§cThat menu does not exist!");
            return true;
        }

        Player targetPlayerJava = preTargetPlayerJava;
        if(targetPlayerJava != null && FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId()) != null) {
            TextFormatter textFormatter = new TextFormatter();
            FloodgatePlayer targetPlayer = FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId());
            try {
                HashMap<String, String> hashMap = json.jsonToHashMap(file);
                String title = textFormatter.formatColorCodes(textFormatter.formatPlaceholders(hashMap.get("Title").replace("{player}", targetPlayer.getCorrectUsername()), targetPlayerJava));
                String body = textFormatter.formatColorCodes(textFormatter.formatPlaceholders(hashMap.get("Body").replace("{player}", targetPlayer.getCorrectUsername()), targetPlayerJava));
                if(hashMap.containsKey("First Button Action") && !(hashMap.containsKey("button-1")))
                {
                    HashMap<String,String> reformattedHashMap = new HashMap<>();
                    reformattedHashMap.put("Menu Name", hashMap.get("Menu Name"));
                    reformattedHashMap.put("Menu Body", hashMap.get("Menu Body"));
                    reformattedHashMap.put("button-1", hashMap.get("First Button Name"));
                    reformattedHashMap.put("button-action-1", hashMap.get("First Button Action"));
                    if(hashMap.containsKey("Second Button Action"))
                    {
                        reformattedHashMap.put("button-2", hashMap.get("Second Button Name"));
                        reformattedHashMap.put("button-action-2", hashMap.get("Second Button Action"));
                        reformattedHashMap.put("Buttons Amount", "2");
                    }else {
                        reformattedHashMap.put("Buttons Amount", "1");
                    }
                    hashMap = reformattedHashMap;
                sender.sendMessage("§aSending menu using §fold formatter§a!");
                }
                int buttons = Integer.parseInt(hashMap.get("Buttons Amount"));
                SimpleForm.Builder modalForm = formBuilder(title,body);
                for (int i = 1; i <= buttons; i++) {
                    modalForm = modalForm.button(new TextFormatter().formatColorCodes(hashMap.get("button-"+ i)));
                }
                modalForm.validResultHandler(result -> {
                    try {
                        resultHandler(result.clickedButtonId(), file, targetPlayerJava, consoleCommand);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
                targetPlayer.sendForm(modalForm);
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
            int argument = args.length-1;
            if(args[argument].isEmpty())
            {
                return menus.getListOfMenus();
            }
            return menus.getListOfMenusContains(args[argument]);

        }
        return null;
    }
    private void resultHandler(int button, File menu, Player player, boolean console) throws FileNotFoundException
    {
        Json json = new Json();
        button = button + 1;
        HashMap<String, String> menuReader =  json.jsonToHashMap(menu);
        String action = menuReader.get("button-action-"+button);
        if(menuReader.containsKey("First Button Action") && button == 1)
        {
            action = menuReader.get("First Button Action");

        }
        if(menuReader.containsKey("Second Button Action") && button == 2)
        {
            action = menuReader.get("Second Button Action");
        }
        TextFormatter textFormatter = new TextFormatter();
        if(action.charAt(0) == '/')
        {
            if(console)
            {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), textFormatter.fullTextFormat(action, player).replace("/", ""));
            }else {
                player.performCommand(textFormatter.fullTextFormat(action, player).replace("/", ""));
            }
        }
        else {
            player.sendMessage(textFormatter.formatPlaceholders(textFormatter.formatColorCodes(action),player));
        }
    }
    private SimpleForm.Builder formBuilder(String title, String body)
    {
        return SimpleForm.builder()
                .title(title)
                .content(body);
    }

}
