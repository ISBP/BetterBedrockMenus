package xyz.pbsi.betterBedrockMenus.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.util.List;
import java.util.NoSuchElementException;

public class ChestInteract implements Listener {
    @EventHandler
    public void onClick (InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();

        if(player.hasMetadata("opened-menu") && event.getView().getTitle().equals("§3§lMenu Creator"))
        {
            event.setCancelled(true);
            ClickType type =  event.getClick();
            switch(event.getSlot())
            {
                case 0:
                    setupMenu("file-name", player);
                    break;
                case 1:
                    setupMenu("menu-name", player);
                    break;
                case 2:
                    setupMenu("menu-text", player);
                    break;
                case 5:
                    confirm(player);
                    break;
                case 6:
                    reset(player);
                    player.closeInventory();
                    player.sendMessage("§cReset your progress!");
                    break;
            }
            if(type.isRightClick() && event.getSlot() == 3)
            {
                setupMenu("first-button-action", player);
                player.sendMessage("§aStart a message with §f!§a if it's a command!");
            } else if (event.getSlot()==3) {
                setupMenu("first-button-name", player);
            }
            if(type.isRightClick() && event.getSlot() == 4)
            {
                setupMenu("second-button-action", player);
                player.sendMessage("§aStart a message with §f!§a if it's a command!");
            } else if (event.getSlot()==4) {
                setupMenu("second-button-name", player);
            }
        }
    }
    @EventHandler
    public void onClose (InventoryCloseEvent event)
    {
        Player player = (Player) event.getPlayer();
        if(player.hasMetadata("opened-menu"))
        {
            player.removeMetadata("opened-menu", BetterBedrockMenus.getInstance());
        }
    }
    public void setupMenu(String value, Player player)
    {
        player.setMetadata("setting-value", new FixedMetadataValue(BetterBedrockMenus.getInstance(), value));
        player.sendMessage("§aPlease a value for§f " +value.replace("-", " ") + "§a! Type §fCancel§a to cancel.");
        player.closeInventory();
    }
    public void confirm(Player player)
    {
        if(!(player.hasMetadata("file-name")) || !(player.hasMetadata("menu-name")) || !(player.hasMetadata("menu-text")))
        {
            player.sendMessage("§cThe menu was not completed!");
            player.closeInventory();
            player.performCommand("menu-creator");
            return;
        }
        List<String> listOfMenus = new Menus().getListOfMenus();


        String fileName = player.getMetadata("file-name").getFirst().asString().replace(" ","-");
        if(listOfMenus.contains(fileName))
        {
            player.sendMessage("§cThat menu already exists!");
            player.closeInventory();
            player.performCommand("menu-creator");
            return;
        }
        String menuName = player.getMetadata("menu-name").getFirst().asString();
        String menuText = player.getMetadata("menu-text").getFirst().asString();
        String buttonName;
        String buttonAction;
        String buttonSecondName;
        String buttonSecondAction;
        try {
            if (player.hasMetadata("second-button-name")) {
                buttonName = player.getMetadata("first-button-name").getFirst().asString();
                buttonAction = player.getMetadata("first-button-action").getFirst().asString();
                buttonSecondName = player.getMetadata("second-button-name").getFirst().asString();
                buttonSecondAction = player.getMetadata("second-button-action").getFirst().asString();
                buttonSecondAction = formatCommand(buttonSecondAction);
                buttonAction = formatCommand(buttonAction);
                player.performCommand("create-menu " + fileName + " " + menuName + " ^" + menuText + " ^" + buttonName + " ^" + buttonAction + " ^" + buttonSecondName + " ^" + buttonSecondAction);
            } else if (player.hasMetadata("first-button-name")) {
                buttonName = player.getMetadata("first-button-name").getFirst().asString();
                buttonAction = player.getMetadata("first-button-action").getFirst().asString();
                buttonAction = formatCommand(buttonAction);
                player.performCommand("create-menu " + fileName + " " + menuName + " ^" + menuText + " ^" + buttonName + " ^" + buttonAction);
            } else {
                player.performCommand("create-menu " + fileName + " " + menuName + " ^" + menuText);
            }
        }catch(NoSuchElementException e)
        {
            player.performCommand("create-menu " + fileName + " " + menuName + " ^" + menuText);
        }

        player.closeInventory();
        reset(player);
    }
    public void reset(Player player)
    {
        player.removeMetadata("file-name", BetterBedrockMenus.getInstance());
        player.removeMetadata("menu-name", BetterBedrockMenus.getInstance());
        player.removeMetadata("menu-text", BetterBedrockMenus.getInstance());
        player.removeMetadata("first-button-name", BetterBedrockMenus.getInstance());
        player.removeMetadata("first-button-action", BetterBedrockMenus.getInstance());
        player.removeMetadata("second-button-name", BetterBedrockMenus.getInstance());
        player.removeMetadata("second-button-action", BetterBedrockMenus.getInstance());
        player.removeMetadata("opened-menu", BetterBedrockMenus.getInstance());
    }

    public String formatCommand(String input)
    {
        if (input.charAt(0) == '!') {
            StringBuilder inputFormatted = new StringBuilder(input);
            inputFormatted.setCharAt(0, '/');
            return inputFormatted.toString();
        }
        return input;
    }
}
