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
        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");
        TextFormatter textFormatter = new TextFormatter();
        if(args.length < 3)
        {
            return false;
        }
        File fileName = new File(folder + "/" + args[0] + ".json");
        if(fileName.exists())
        {
            sender.sendMessage("§cThat menu already exists!");
            return true;
        }
        int building = 0;
        StringBuilder titleBuilder = new StringBuilder();
        StringBuilder bodyBuilder = new StringBuilder();

        for (int i = 1; i < args.length; i++)
        {
            if(args[i].charAt(0) == '^')
            {
                building = building + 1;
            }
            if(building == 0)
            {
                titleBuilder.append(args[i]).append(" ");
            }
            if (building == 1)
            {
                bodyBuilder.append(args[i]).append(" ");
            }
        }
        if(building == 0)
        {
            sender.sendMessage("§cNo body defined! Start an argument with ^ to start the body string!");
            return true;
        }

        JsonObject object = new JsonObject();
        object.addProperty("Title", textFormatter.formatColorCodes(titleBuilder.toString().replace("^", "")));
        object.addProperty("Body", textFormatter.formatColorCodes(bodyBuilder.toString().replace("^", "")));
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
