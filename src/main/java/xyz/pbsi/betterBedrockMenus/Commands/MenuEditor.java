package xyz.pbsi.betterBedrockMenus.Commands;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Json;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuEditor implements CommandExecutor, TabCompleter {
    Menus menus = new Menus();
    Json json = new Json();
    Gson gson = new Gson();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length < 3)
        {
            return false;
        }
        if(!menus.isMenu(args[0]))
        {
            sender.sendMessage("§cThat menu does not exist! (Case Sensitive)");
            return true;
        }
        String method = args[1];
        if(args[1].contains("Button-"))
        {
            method = method.toLowerCase();
        }
        try {
            File menuFile = new File(BetterBedrockMenus.getInstance().getDataFolder() + "/menus/" + args[0] + ".json");
            HashMap<String, String> menuJSON = json.jsonToHashMap(menuFile);
            StringBuilder newValue = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                newValue.append(args[i]).append(" ");
            }
            newValue.deleteCharAt(newValue.length()-1);
            menuJSON.put(method, newValue.toString());
            Type typeObject = new TypeToken<HashMap>() {}.getType();
            String gsonData = gson.toJson(menuJSON, typeObject);
            FileWriter writer = new FileWriter(menuFile);
            writer.write(gsonData);
            writer.close();
            sender.sendMessage("§aSet §f"+method+"§a to §f"+ newValue + "§a in menu§f "+ args[0] + "§a!");

        } catch (IOException e) {
            BetterBedrockMenus.getInstance().getLogger().severe("An error has occurred; please report this to the developer. " + e.getMessage());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1)
        {
            if(args[0].isEmpty())
            {
                return menus.getListOfMenus();
            }
            return menus.getListOfMenusContains(args[0]);
        }
        if(args.length == 2)
        {
            int buttons = 0;
            if(menus.isMenu(args[0]))
            {
                try {
                    HashMap<String, String> menuJSON =  json.jsonToHashMap(new File(BetterBedrockMenus.getInstance().getDataFolder() + "/menus/"+args[0]+".json"));
                    if(menuJSON.containsKey("Buttons Amount"))
                    {
                        buttons = Integer.parseInt(menuJSON.get("Buttons Amount"));
                    }
                    else if(menuJSON.containsKey("Second Button Action")){
                        buttons = 2;
                    } else if (menuJSON.containsKey("First Button Action")) {
                        buttons = 1;
                    }
                } catch (FileNotFoundException e) {
                    BetterBedrockMenus.getInstance().getLogger().warning(e.getMessage());
                    return List.of("Error");
                }

            }
            ArrayList<String> methods = new ArrayList<>();
            methods.add("Title");
            methods.add("Body");
            for (int i = 1; i <= buttons; i++) {
                methods.add("Button-"+i);
                methods.add("Button-Action-"+i);
            }
            return methods;
        }

        return null;
    }
}
