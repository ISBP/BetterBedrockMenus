package xyz.pbsi.betterBedrockMenus.Utils;

import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Menus {
    //Returns a list of the menus by reading the menus directory and removes the .json in the file names.
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

    public List<String> getListOfMenusContains(String string)
    {
        Menus menus = new Menus();
            string = string.toLowerCase();
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < menus.getListOfMenus().size(); i++) {
                if (getListOfMenus().get(i).toLowerCase().contains(string)) {
                    arrayList.add(menus.getListOfMenus().get(i));
                }
            }
            return arrayList;


    }
}
