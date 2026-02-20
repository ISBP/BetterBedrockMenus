package xyz.pbsi.betterBedrockMenus.Commands;

import com.google.gson.JsonObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.TextFormatter;


import java.io.*;

public class MenuCreator implements CommandExecutor {
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
        StringBuilder buttonAction = new StringBuilder();
        StringBuilder buttonName = new StringBuilder();
        //String buttonName = null;
        for (int i = 1; i < args.length; i++)
        {
            //Checks to see whether someone is creating a new section
            if(args[i].charAt(0) == '^')
            {
                building = building + 1;
            }
            if(args[i].charAt(0) == '-')
            {
                //Will be used for command arguments
                i++;
            }
            //Will be replaced with a switch eventually ;-;

            if(building == 0)
            {
                titleBuilder.append(args[i]).append(" ");
            }
            if (building == 1)
            {
                bodyBuilder.append(args[i]).append(" ");
            }
            //Buttons will become a hashmap to allow for greater additions, this is just temporary
            if(building == 2)
            {
                buttonName.append(args[i]).append(" ");
            }
            if(building == 3)
            {
                buttonAction.append(args[i]).append(" ");
            }
        }
        if(building == 0)
        {
            sender.sendMessage("§cNo body defined! Start an argument with ^ to start the body string!");
            return true;
        }
        if(building == 2)
        {
            sender.sendMessage("§cNo action defined for new button!");
            return true;
        }
        //Removes the trailing space
        titleBuilder.deleteCharAt(titleBuilder.length() - 1);
        bodyBuilder.deleteCharAt(bodyBuilder.length() - 1);
        if(!(buttonName.isEmpty()))
        {
            buttonName.deleteCharAt(buttonName.length() - 1);
            buttonAction.deleteCharAt(buttonAction.length() - 1);

        }

        JsonObject object = new JsonObject();
        object.addProperty("Title", textFormatter.formatColorCodes(titleBuilder.toString().replace("^", "")));
        object.addProperty("Body", textFormatter.formatColorCodes(bodyBuilder.toString().replace("^", "")));
        object.addProperty("Button Name", textFormatter.formatColorCodes(buttonName.toString().replace("^","")));
        object.addProperty("Button Action", textFormatter.formatColorCodes(buttonAction.toString().replace("^", "")));
        try {
            FileWriter file = new FileWriter(folder + "/"+args[0]+".json");
            file.write(object.toString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sender.sendMessage(object.toString());
        sender.sendMessage("§aSuccessfully generated menu!");

        return true;
    }
}
