package xyz.pbsi.betterBedrockMenus.Utils;

import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Menus {
    public List<String> getListOfMenus()
    {
        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");
        List<String> arguments = new ArrayList<>();
        String[] folderList = folder.list();
        if(folderList != null)
        {

            for (int i = 0; i < folderList.length; i++) {
                String formattedArg = folderList[i].replace(".json","");
                arguments.add(formattedArg);
            }

        }
        return arguments;

    }
}
