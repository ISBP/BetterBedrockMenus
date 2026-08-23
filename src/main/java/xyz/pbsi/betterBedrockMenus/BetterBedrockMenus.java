package xyz.pbsi.betterBedrockMenus;

import ch.njol.skript.Skript;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;
import xyz.pbsi.betterBedrockMenus.Commands.*;
import xyz.pbsi.betterBedrockMenus.Listeners.ChatListener;
import xyz.pbsi.betterBedrockMenus.Listeners.ChestInteract;
import xyz.pbsi.betterBedrockMenus.Listeners.PlayerJoin;
import xyz.pbsi.betterBedrockMenus.Skript.Module;
import xyz.pbsi.betterBedrockMenus.Utils.Metrics;
import xyz.pbsi.betterBedrockMenus.Utils.UpdateChecker;

import java.io.File;
import java.util.Objects;

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
        //BStats is a separate project, and thus, the project license does not apply to it.
        if(!getPluginMeta().getVersion().contains("beta"))
        {
            new Metrics(this, pluginId);
            new UpdateChecker().checkForUpdates();
        }
        //Performs setup
        createConfig();
        registerCommands();
        registerEvents();
        setupSkript();
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
        Objects.requireNonNull(this.getCommand("BetterBedrockMenus")).setExecutor(new Info());
        Objects.requireNonNull(this.getCommand("Send-Command-Menu")).setExecutor(new CommandMenuSender());
        Objects.requireNonNull(this.getCommand("Create-Menu")).setExecutor(new MenuCreator());
        Objects.requireNonNull(this.getCommand("Delete-Menu")).setExecutor(new MenuDeleter());
        Objects.requireNonNull(this.getCommand("Send-Menu")).setExecutor(new MenuSender());
        Objects.requireNonNull(this.getCommand("Menu-Creator")).setExecutor(new MenuUI());
        Objects.requireNonNull(this.getCommand("Open-Menu")).setExecutor(new OpenMenu());
        Objects.requireNonNull(this.getCommand("Edit-Menu")).setExecutor(new MenuEditor());
        Objects.requireNonNull(this.getCommand("List-Menus")).setExecutor(new ListMenus());
    }
    public void setupSkript()
    {
        if(!(Bukkit.getPluginManager().isPluginEnabled("Skript"))) return;
        SkriptAddon addon = Skript.instance().registerAddon(BetterBedrockMenus.class, "BetterBedrockMenus");
        addon.loadModules(new Module());

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
                config.addDefault("Welcome Menu File", "None");
                config.addDefault("Self Menu Open", false);
                config.addDefault("Welcome Menu Console", false);
                config.addDefault("Log Sent Menus", false);
                config.addDefault("Close Button", true);
                config.addDefault("Version", 3);
                config.options().copyDefaults(true);
                saveConfig();
            }
            if(config.getInt("Version") < 3)
            {
                //Config version checker
                getLogger().severe("Your configuration is out of date! Please delete it and restart the server.");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        if(!menuDirectory.exists())
        {
            if(!menuDirectory.mkdir())
            {
                getLogger().severe("Failed to create menu directory!");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    public static BetterBedrockMenus getInstance()
    {
        return INSTANCE;
    }
}
