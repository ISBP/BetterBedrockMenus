package xyz.pbsi.betterBedrockMenus.Listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        FileConfiguration config = BetterBedrockMenus.getInstance().getConfig();
        //Formats alternate color codes
        String welcomeTitle = ChatColor.translateAlternateColorCodes('&', config.getString("Welcome Menu Title"));
        String welcomeText = ChatColor.translateAlternateColorCodes('&', config.getString("Welcome Menu Message"));
        if(config.getBoolean("Welcome Menu Enabled"))
        {
            SimpleForm.Builder welcomeForm = SimpleForm.builder()
                    .title(welcomeTitle)
                    .content(welcomeText)
                    .button("Close");
            Player javaPlayer = event.getPlayer();
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(javaPlayer.getUniqueId());
            if(player != null)
            {   //The sending of the form is delayed to allow the client to initially load.
                //Sending it early will result in the menu not displaying.
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        player.sendForm(welcomeForm);
                    }
                }.runTaskLater(BetterBedrockMenus.getInstance(), 20L);
            }
        }



    }


}
