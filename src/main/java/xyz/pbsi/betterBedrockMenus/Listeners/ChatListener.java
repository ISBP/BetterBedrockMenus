package xyz.pbsi.betterBedrockMenus.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
//Deprecation error occurs just because I'm using paper and it hates me
@SuppressWarnings("deprecation")
public class ChatListener implements Listener {
    @EventHandler
    public void onChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(player.hasMetadata("setting-value"))
        {
            String value = player.getMetadata("setting-value").getFirst().asString();
            if(event.getMessage().equalsIgnoreCase("cancel"))
            {
                player.removeMetadata("setting-value", BetterBedrockMenus.getInstance());
                event.setCancelled(true);
                player.sendMessage("§cCanceled current operation!");
                return;
            }
            if(event.getMessage().equalsIgnoreCase("reset"))
            {
                player.removeMetadata("setting-value", BetterBedrockMenus.getInstance());
                player.removeMetadata(value, BetterBedrockMenus.getInstance());
                if(player.hasMetadata("setting-button"))
                {
                    int button = player.getMetadata("setting-button").getFirst().asInt();
                    String type = player.getMetadata("setting-type").getFirst().asString();
                    if(type.equals("button-action"))
                    {
                        player.removeMetadata("button-action-"+button, BetterBedrockMenus.getInstance());
                    }else {
                        player.removeMetadata("button-"+button, BetterBedrockMenus.getInstance());
                    }
                    player.removeMetadata("setting-button", BetterBedrockMenus.getInstance());
                    player.removeMetadata("setting-type", BetterBedrockMenus.getInstance());
                }
                event.setCancelled(true);
                player.sendMessage("§cRemoved current value!");
                return;
            }
            if(player.hasMetadata("setting-button"))
            {
                int button = player.getMetadata("setting-button").getFirst().asInt();
                String type = player.getMetadata("setting-type").getFirst().asString();
                if(type.equals("button-action"))
                {
                    player.setMetadata("button-action-"+button, new FixedMetadataValue(BetterBedrockMenus.getInstance(), event.getMessage()));
                    player.sendMessage("§aSet §f"+value.replace("-", " ") + " action §ato §f"+ event.getMessage());

                }
                else {
                    player.setMetadata("button-"+button, new FixedMetadataValue(BetterBedrockMenus.getInstance(), event.getMessage()));
                    player.sendMessage("§aSet §f"+value.replace("-", " ") + " name §ato §f"+ event.getMessage());
                }
                event.setCancelled(true);
                player.removeMetadata("setting-button", BetterBedrockMenus.getInstance());
                player.removeMetadata("setting-value", BetterBedrockMenus.getInstance());
                player.performCommand("menu-creator");
                return;
            }
            player.setMetadata(value, new FixedMetadataValue(BetterBedrockMenus.getInstance(), event.getMessage()));
            player.removeMetadata("setting-value", BetterBedrockMenus.getInstance());
            player.performCommand("menu-creator");
            player.sendMessage("§aSet §f"+value.replace("-", " ") + "§a to §f"+ event.getMessage());
            event.setCancelled(true);
        }
    }
}
