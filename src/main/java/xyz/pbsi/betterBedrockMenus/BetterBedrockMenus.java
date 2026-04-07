package xyz.pbsi.betterBedrockMenus;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.pbsi.betterBedrockMenus.Commands.*;
import xyz.pbsi.betterBedrockMenus.Listeners.ChatListener;
import xyz.pbsi.betterBedrockMenus.Listeners.ChestInteract;
import xyz.pbsi.betterBedrockMenus.Listeners.PlayerJoin;
import xyz.pbsi.betterBedrockMenus.Utils.Metrics;
import xyz.pbsi.betterBedrockMenus.Utils.UpdateChecker;

import java.io.File;
public final class BetterBedrockMenus extends JavaPlugin {
    private static BetterBedrockMenus INSTANCE;

    @Override
    public void onEnable() {
        //Initialize the instance
        INSTANCE = this;
        //Saves the current time for setup duration calculation
        long time = System.currentTimeMillis();
        //Enables BStats
        int pluginId = 29704;
        //BStats is a separate project, and the project license does not apply to it.
        new Metrics(this, pluginId);
        //Performs setup
        createConfig();
        registerCommands();
        registerEvents();
        new UpdateChecker().checkForUpdates();
        //Logs duration
        this.getLogger().info("Successfully enabled Better Bedrock Menus " + (System.currentTimeMillis() - time) + "ms!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Successfully disabled the plugin.");
    }

    public void registerEvents()
    {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new ChestInteract(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }
    public void registerCommands()
    {
        this.getCommand("BetterBedrockMenus").setExecutor(new Info());
        this.getCommand("Send-Command-Menu").setExecutor(new MenuSender());
        this.getCommand("Create-Menu").setExecutor(new MenuCreator());
        this.getCommand("Delete-Menu").setExecutor(new MenuDeleter());
        this.getCommand("Send-Menu").setExecutor(new MenuPremadeSender());
        this.getCommand("Menu-Creator").setExecutor(new MenuUI());
    }

    public void createConfig()
    {
        FileConfiguration config = this.getConfig();
        File menuDirectory = (new File(getDataFolder()+ "/menus") );
        File configFile = new File(this.getDataFolder(), "config.yml");
        //Checks if the main folder exists
        if(!getDataFolder().exists())
        {
            if(!getDataFolder().mkdir())
            {
                //If the function doesn't return true an error has occurred.
                this.getLogger().warning("Failed to create config folder.");
            }

            }
            if(!configFile.exists())
            {
                //Builds and creates the config file
                this.saveDefaultConfig();
                config.addDefault("Welcome Menu Enabled", false);
                config.addDefault("Welcome Menu File", "Welcome");
                config.addDefault("Version", 2);
                config.options().copyDefaults(true);
                saveConfig();

            }
            if(config.getInt("Version") != 2)
            {
                //Config version checker
                getLogger().warning("Your configuration is out of date! Please delete it and restart the server.");
            }
        if(!menuDirectory.exists())
        {
            if(!menuDirectory.mkdir())
            {
                getLogger().warning("Failed to create menu directory!");
            }
        }
    }

    public static BetterBedrockMenus getInstance()
    {
        return INSTANCE;
    }
}
