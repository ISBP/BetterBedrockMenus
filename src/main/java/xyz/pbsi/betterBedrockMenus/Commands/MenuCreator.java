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
        StringBuilder buttonAction = new StringBuilder();
        StringBuilder buttonName = new StringBuilder();
        StringBuilder buttonTwoAction = new StringBuilder();
        StringBuilder buttonTwoName = new StringBuilder();
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
                case 2:
                    buttonName.append(args[i]).append(" ");
                    break;
                case 3:
                    buttonAction.append(args[i]).append(" ");
                    break;
                case 4:
                    buttonTwoName.append(args[i]).append(" ");
                    break;
                case 5:
                    buttonTwoAction.append(args[i]).append(" ");
            }

        }
        //Error catching
        if(building == 0)
        {
            sender.sendMessage("§cNo body defined! Start an argument with ^ to start the body string!");
            return true;
        }
        if(building == 2 || building == 4)
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
        if(!(buttonTwoName.isEmpty()))
        {
            buttonTwoName.deleteCharAt(buttonTwoName.length() - 1);
            buttonTwoAction.deleteCharAt(buttonTwoAction.length() - 1);

        }
        //Adds the JSON objects
        JsonObject object = new JsonObject();
        object.addProperty("Title", textFormatter.formatColorCodes(titleBuilder.toString().replace("^", "")));
        object.addProperty("Body", textFormatter.formatColorCodes(bodyBuilder.toString().replace("^", "")));
        object.addProperty("First Button Name", textFormatter.formatColorCodes(buttonName.toString().replace("^","")));
        object.addProperty("First Button Action", textFormatter.formatColorCodes(buttonAction.toString().replace("^", "")));
        object.addProperty("Second Button Name", textFormatter.formatColorCodes(buttonTwoName.toString().replace("^","")));
        object.addProperty("Second Button Action", textFormatter.formatColorCodes(buttonTwoAction.toString().replace("^", "")));
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
