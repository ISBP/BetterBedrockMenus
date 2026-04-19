package xyz.pbsi.betterBedrockMenus.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menus {
    /**
     * Returns a list of the menus by reading the menus directory and removes the .json in the file names.
     * @return A list of all menus
     */
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

    /**
     * Finds menus that contain a certain string, non-case-sensitive, used for tab completion
     * @param string The phrase to be found from the list of files
     * @return All files that contain the phrase provided, may be empty
     */
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

    /**
     * Checks whether the name provided matches a menu that exists
     * @param name The name to search for
     * @return Whether the name matches a menu
     */
    public boolean isMenu(String name)
    {
        return getListOfMenus().contains(name);
    }

    /**
     * Gets a file from the name of a menu
     * @param name The name of a menu
     * @return The file associated with the menu
     */
    public File getMenuAsFile(String name)
    {
        return new File(BetterBedrockMenus.getInstance().getDataFolder() + "/menus/" + name + ".json");

    }

    /**
     * Updates a menu created before version 0.5 to the modern version
     * @param menuFile The file of the menu that is to be updated
     * @throws IOException Occurs when the file cannot be found or modified
     */
    public void updateMenu(File menuFile) throws IOException {
        Json json = new Json();
        Gson gson = new Gson();
        HashMap<String, String> menuJSON = json.jsonToHashMap(menuFile);
        if(menuJSON.containsKey("buttons-amount"))
        {
            return;
        }
        int buttons = 0;
        if(menuJSON.containsKey("Second Button Name"))
        {
            menuJSON.put("button-1", menuJSON.get("First Button Name"));
            menuJSON.put("button-action-1", menuJSON.get("First Button Action"));
            menuJSON.put("button-2", menuJSON.get("Second Button Name"));
            menuJSON.put("button-action-2", menuJSON.get("Second Button Action"));
            buttons = 2;
        } else if (menuJSON.containsKey("First Button Name")) {
            menuJSON.put("button-1", menuJSON.get("First Button Name"));
            menuJSON.put("button-action-1", menuJSON.get("First Button Action"));
            buttons = 1;
        }
        menuJSON.put("Buttons Amount", String.valueOf(buttons));
        menuJSON.remove("First Button Name");
        menuJSON.remove("First Button Action");
        menuJSON.remove("Second Button Name");
        menuJSON.remove("Second Button Action");
        Type typeObject = new TypeToken<HashMap>() {}.getType();
        String gsonData = gson.toJson(menuJSON, typeObject);
        FileWriter writer = new FileWriter(menuFile);
        writer.write(gsonData);
        writer.close();
    }
}
