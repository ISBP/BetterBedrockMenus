package xyz.pbsi.betterBedrockMenus;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.pbsi.betterBedrockMenus.Commands.*;
import xyz.pbsi.betterBedrockMenus.Listeners.PlayerJoin;

import java.io.File;

public final class BetterBedrockMenus extends JavaPlugin {
    private static BetterBedrockMenus INSTANCE;

    @Override
    public void onEnable() {
        createConfig();
        registerCommands();
        registerEvents();
        INSTANCE = this;
        this.getLogger().info("Has successfully initialized!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents()
    {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }
    public void registerCommands()
    {
        this.getCommand("BetterBedrockMenus").setExecutor(new Info());
        this.getCommand("Send-Menu").setExecutor(new MenuSender());
        this.getCommand("Create-Menu").setExecutor(new MenuCreator());
        this.getCommand("Delete-Menu").setExecutor(new MenuDeleter());
        this.getCommand("Send-Premade-Menu").setExecutor(new MenuPremadeSender());
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
