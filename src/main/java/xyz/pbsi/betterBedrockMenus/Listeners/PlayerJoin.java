package xyz.pbsi.betterBedrockMenus.Listeners;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;
public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player javaPlayer = event.getPlayer();
        FileConfiguration config = BetterBedrockMenus.getInstance().getConfig();
        //Formats alternate color code
        if(config.getBoolean("Welcome Menu Enabled") && FloodgateApi.getInstance().isFloodgatePlayer(javaPlayer.getUniqueId()))
        {
            String menu = config.getString("Welcome Menu File");
            if(new Menus().getListOfMenus().contains(menu))
            {
                FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(javaPlayer.getUniqueId());
                if(player != null)
                {   //The sending of the form is delayed to allow the client to initially load.
                    //Sending it early will result in the menu not displaying.
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "send-menu "+ menu + " " + javaPlayer.getName());
                        }
                    }.runTaskLater(BetterBedrockMenus.getInstance(), 60L);
                }
            }
            else {
                BetterBedrockMenus.getInstance().getLogger().warning("The welcome menu file doesn't exist!");
            }

        }



    }


}
