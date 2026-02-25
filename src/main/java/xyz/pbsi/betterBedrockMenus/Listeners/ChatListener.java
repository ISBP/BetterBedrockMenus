package xyz.pbsi.betterBedrockMenus.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
//Despite being deprecated the methods used are fine and work the best out of all the option soooo o.o
//Yay I love coding at 1
@SuppressWarnings("deprecation")
public class ChatListener implements Listener {
    @EventHandler
    public void onChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(player.hasMetadata("setting-value"))
        {
            if(event.getMessage().equalsIgnoreCase("cancel"))
            {
                player.removeMetadata("setting-value", BetterBedrockMenus.getInstance());
                event.setCancelled(true);
                player.sendMessage("&cCanceled operation.");
            }
            String value = player.getMetadata("setting-value").getFirst().asString();
            player.setMetadata(value, new FixedMetadataValue(BetterBedrockMenus.getInstance(), event.getMessage()));
            player.removeMetadata("setting-value", BetterBedrockMenus.getInstance());
            player.performCommand("menu-creator");
            player.sendMessage("§aSet §f"+value.replace("-", " ") + "§a to §f"+ event.getMessage());
            event.setCancelled(true);
        }
    }
}
