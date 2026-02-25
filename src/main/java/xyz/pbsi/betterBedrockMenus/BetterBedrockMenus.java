package xyz.pbsi.betterBedrockMenus;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.pbsi.betterBedrockMenus.Commands.*;
import xyz.pbsi.betterBedrockMenus.Listeners.ChatListener;
import xyz.pbsi.betterBedrockMenus.Listeners.ChestInteract;
import xyz.pbsi.betterBedrockMenus.Listeners.PlayerJoin;
import xyz.pbsi.betterBedrockMenus.Utils.Metrics;

import java.io.File;
public final class BetterBedrockMenus extends JavaPlugin {
    private static BetterBedrockMenus INSTANCE;

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        int pluginId = 29704;
        new Metrics(this, pluginId);
        createConfig();
        registerCommands();
        registerEvents();
        INSTANCE = this;
        this.getLogger().info("Successfully enabled the plugin in " + (System.currentTimeMillis() - time) + "ms!");
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
        if(!getDataFolder().exists())
        {
            if(!getDataFolder().mkdir())
            {
                this.getLogger().warning("Failed to create config folder.");
            }

            File configFile = new File(this.getDataFolder(), "config.yml");
            if(!configFile.exists())
            {
                this.saveDefaultConfig();
                config.addDefault("Welcome Menu Enabled", true);
                config.addDefault("Welcome Menu Title", "Welcome to example server!");
                config.addDefault("Welcome Menu Message", "Welcome to the server! For more info type /help!");
                config.addDefault("Version", 1);
                config.options().copyDefaults(true);
                saveConfig();

            }

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
