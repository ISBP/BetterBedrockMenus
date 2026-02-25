package xyz.pbsi.betterBedrockMenus.Listeners;


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
import xyz.pbsi.betterBedrockMenus.Utils.TextFormatter;

public class PlayerJoin implements Listener {
    TextFormatter textFormatter = new TextFormatter();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        FileConfiguration config = BetterBedrockMenus.getInstance().getConfig();
        //Formats alternate color codes
        String welcomeTitle = textFormatter.formatPlaceholders(textFormatter.formatColorCodes(config.getString("Welcome Menu Title")), event.getPlayer());
        String welcomeText = textFormatter.formatPlaceholders(textFormatter.formatColorCodes(config.getString("Welcome Menu Message")), event.getPlayer());
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
