package xyz.pbsi.betterBedrockMenus.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.Nullable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Callbacks.FileReadCallback;

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
    public static List<String> getListOfMenus()
    {
        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");
        List<String> arguments = new ArrayList<>();
        String[] folderList = folder.list();
        if(folderList != null)
        {
            for (String s : folderList) {
                String formattedArg = s.replace(".json", "");
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
    public static List<String> getListOfMenusContains(String string)
    {
            string = string.toLowerCase();
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < Menus.getListOfMenus().size(); i++) {
                if (getListOfMenus().get(i).toLowerCase().contains(string)) {
                    arrayList.add(Menus.getListOfMenus().get(i));
                }
            }
            return arrayList;
    }

    /**
     * Checks whether the name provided matches a menu that exists
     * @param name The name to search for
     * @return Whether the name matches a menu
     */
    public static boolean isMenu(String name)
    {
        return getListOfMenus().contains(name);
    }

    /**
     * Gets a file from the name of a menu
     * @param name The name of a menu
     * @return The file associated with the menu
     */
    public static File getMenuAsFile(String name)
    {
        return new File(BetterBedrockMenus.getInstance().getDataFolder() + "/menus/" + name + ".json");
    }

    /**
     * Updates a menu created before version 0.5 to the modern version
     * @param menuFile The file of the menu that is to be updated
     * @throws IOException Occurs when the file cannot be found or modified
     */
    public static void updateMenu(File menuFile) throws IOException {
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

    /**
     *
     * @param sender The command sender.
     * @param targetPlayerJava The Java Player to send the command to.
     * @param fileName The name of the file of the menu being sent.
     * @param consoleCommand Whether to execute commands inside the menu as console.
     */
    public static void sendMenu(@Nullable CommandSender sender, Player targetPlayerJava, String fileName, boolean consoleCommand)
    {
        File folder = new File(BetterBedrockMenus.getInstance().getDataFolder()+"/menus");

        File file = new File(folder + "/" + fileName + ".json");
        if(sender == null)
        {
            //Logs all messages to console if the sender is null
            sender = Bukkit.getConsoleSender();
        }
        if(!file.exists())
        {
            sender.sendMessage("§cThat menu does not exist!");
            return;
        }

        if(targetPlayerJava != null && FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId()) != null) {
            TextFormatter textFormatter = new TextFormatter();
            FloodgatePlayer targetPlayer = FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId());
            try {
                @Nullable CommandSender finalSender = sender;
                FileIO.readFileAsync(file, new FileReadCallback() {
                    @Override
                    public void onFileRead(HashMap<String, String> hashMap) {
                        String title = textFormatter.formatColorCodes(textFormatter.formatPlaceholders(hashMap.get("Title").replace("{player}", targetPlayer.getCorrectUsername()), targetPlayerJava));
                        String body = textFormatter.formatColorCodes(textFormatter.formatPlaceholders(hashMap.get("Body").replace("{player}", targetPlayer.getCorrectUsername()), targetPlayerJava));
                        if(!hashMap.containsKey("Buttons Amount"))
                        {
                            finalSender.sendMessage("§aAttempting to update menu format....");
                            try
                            {
                                Menus.updateMenu(file);
                            } catch (IOException e) {
                                finalSender.sendMessage("§cAn error occurred whilst trying to send this menu! Is it formatted correctly?");
                                BetterBedrockMenus.getInstance().getLogger().severe(e.getMessage());
                                return;
                            }
                            finalSender.sendMessage("§aUpdated menu format!");
                        }
                        int buttons = 0;
                        if(hashMap.containsKey("Buttons Amount"))
                        {
                            buttons = Integer.parseInt(hashMap.get("Buttons Amount"));
                        }

                        SimpleForm.Builder modalForm = formBuilder(title,body);
                        for (int i = 1; i <= buttons; i++) {
                            modalForm = modalForm.button(new TextFormatter().formatColorCodes(hashMap.get("button-"+ i)));
                        }
                        if(BetterBedrockMenus.getInstance().getConfig().getBoolean("Close Button"))
                        {
                            modalForm = modalForm.button("Close");
                        }
                        modalForm.validResultHandler(result -> {
                            try {
                                resultHandler(result.clickedButtonId(), file, targetPlayerJava, consoleCommand);
                            } catch (FileNotFoundException e) {
                                BetterBedrockMenus.getInstance().getLogger().severe(e.getMessage());
                            }
                        });
                        if(BetterBedrockMenus.getInstance().getConfig().getBoolean("Log Sent Menus"))
                        {
                            BetterBedrockMenus.getInstance().getLogger().info(finalSender.getName() + " sent menu "+ hashMap.get("Menu Name") + " to " + targetPlayerJava.getName()+".");
                        }
                        targetPlayer.sendForm(modalForm);
                    }
                });

            } catch (IOException e) {
                BetterBedrockMenus.getInstance().getLogger().severe(e.getMessage());
            }

        }
    }
    private static void resultHandler(int button, File menu, Player player, boolean console) throws FileNotFoundException
    {
        button = button + 1;
        HashMap<String, String> menuReader =  Json.jsonToHashMap(menu);
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
        try
        {
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
        }catch (NullPointerException e)
        {
            if(!BetterBedrockMenus.getInstance().getConfig().getBoolean("Close Button"))
            {
                BetterBedrockMenus.getInstance().getLogger().info(menu.getName() + " appears to lack an action for button" +button);
            }
        }

    }
    private static SimpleForm.Builder formBuilder(String title, String body)
    {
        return SimpleForm.builder()
                .title(title)
                .content(body);
    }
}
