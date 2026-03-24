package xyz.pbsi.betterBedrockMenus.Commands;

import com.google.gson.JsonObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.TextFormatter;


import java.io.*;
import java.util.HashMap;
import java.util.List;

public class MenuCreator implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        //Data folder where the menus are stored in, located at /plugins/BetterBedrockMenus/menus
        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");
        TextFormatter textFormatter = new TextFormatter();
        if(args.length < 3)
        {
            return false;
        }
        //File for the new menu
        File fileName = new File(folder + "/" + args[0] + ".json");
        if(fileName.exists())
        {
            sender.sendMessage("§cThat menu already exists!");
            return true;
        }
        int building = 0;
        StringBuilder titleBuilder = new StringBuilder();
        StringBuilder bodyBuilder = new StringBuilder();
        JsonObject object = new JsonObject();

        HashMap<String, String> buttonBuilderHashMap = new HashMap<String, String>();
        int amountOfButtons = 0;
        //String buttonName = null;
        for (int i = 1; i < args.length; i++)
        {
            //Checks to see whether someone is creating a new section
            if(args[i].charAt(0) == '^')
            {
                building = building + 1;
            }
            //Determines what one is editing and appends the string to the relevant variable.
            switch (building)
            {
                case 0:
                    titleBuilder.append(args[i]).append(" ");
                    break;
                case 1:
                    bodyBuilder.append(args[i]).append(" ");
                    break;
            }
            if(building > 1)
            {

                if(building % 2 == 0)
                {
                    if(buttonBuilderHashMap.containsKey("button-"+amountOfButtons) && !args[i].contains("^"))
                    {
                        buttonBuilderHashMap.put("button-"+amountOfButtons, buttonBuilderHashMap.get("button-"+amountOfButtons) + args[i] + " ");
                    }else{
                        amountOfButtons = amountOfButtons + 1;
                        buttonBuilderHashMap.put("button-" + amountOfButtons, args[i] + " ");
                    }
                }
                else {
                    if(buttonBuilderHashMap.containsKey("button-action-"+amountOfButtons))
                    {
                        buttonBuilderHashMap.put("button-action-"+amountOfButtons, buttonBuilderHashMap.get("button-action-"+amountOfButtons) + args[i] + " ");
                    }
                    else {
                        buttonBuilderHashMap.put("button-action-" + amountOfButtons, args[i] + " ");
                    }
                }
            }

        }
        //Removes the trailing space
        titleBuilder.deleteCharAt(titleBuilder.length() - 1);
        bodyBuilder.deleteCharAt(bodyBuilder.length() - 1);
        object.addProperty("Title", textFormatter.formatColorCodes(titleBuilder.toString().replace("^", "")));
        object.addProperty("Body", textFormatter.formatColorCodes(bodyBuilder.toString().replace("^", "")));
        object.addProperty("Buttons Amount", String.valueOf(amountOfButtons));

        //Error catching
        if(building == 0)
        {
            sender.sendMessage("§cNo body defined! Start an argument with ^ to start the body string!");
            return true;
        }
        if(building > 1 && building % 2 == 0)
        {
            sender.sendMessage("§cNo action defined for new button!");
            return true;
        }

        for (int i = 1; i <= amountOfButtons; i++) {
            StringBuilder buttonName = new StringBuilder(buttonBuilderHashMap.get("button-"+i));
            StringBuilder buttonAction = new StringBuilder(buttonBuilderHashMap.get("button-action-"+i));
            if(!(buttonName.isEmpty()) && !(buttonAction.isEmpty()))
            {
                buttonName.deleteCharAt(buttonName.length() - 1);
                buttonAction.deleteCharAt(buttonAction.length() - 1);
            }
            else
            {
                sender.sendMessage("ok so smth broke ngl");
                return true;
            }
            object.addProperty("button-" + i, buttonName.toString().replace("^", ""));
            object.addProperty("button-action-" + i, buttonAction.toString().replace("^", ""));

        }

        //Adds the JSON objects

        try {
            FileWriter file = new FileWriter(folder + "/"+args[0]+".json");
            file.write(object.toString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sender.sendMessage("§aSuccessfully generated menu!");

        return true;
    }
    //Tab completion, set's the first position to FileName then provides a brief template for the other positions.
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1)
        {
            return List.of("FileName");
        }
        return List.of("Menu Title ^Menu Body ^Button Name ^Button Action ^Button Name ^Button Action");
    }
}
