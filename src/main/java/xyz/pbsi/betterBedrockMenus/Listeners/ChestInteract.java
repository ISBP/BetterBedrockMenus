package xyz.pbsi.betterBedrockMenus.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.util.List;
import java.util.NoSuchElementException;
@SuppressWarnings("deprecation")
public class ChestInteract implements Listener {
    @EventHandler
    public void onClick (InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(player.hasMetadata("menu-error"))
        {
            event.setCancelled(true);
            player.closeInventory();
            player.removeMetadata("menu-error", BetterBedrockMenus.getInstance());
            player.performCommand("menu-creator");
            return;
        }

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
                case 7:
                    confirm(player);
                    break;
                case 8:
                    reset(player);
                    player.closeInventory();
                    player.sendMessage("§cReset your progress!");
                    break;
            }
                int button = event.getSlot();
                if(button > 2 && button < 7)
                {
                    player.closeInventory();
                    button = button - 2;
                    if(player.hasMetadata("buttons") && player.getMetadata("buttons").getFirst().asInt() < button)
                    {
                        player.setMetadata("buttons", new FixedMetadataValue(BetterBedrockMenus.getInstance(), button));

                    }else if (!player.hasMetadata("buttons")){
                        player.setMetadata("buttons", new FixedMetadataValue(BetterBedrockMenus.getInstance(), button));
                    }
                    player.setMetadata("setting-button", new FixedMetadataValue(BetterBedrockMenus.getInstance(), button));
                    player.setMetadata("setting-value", new FixedMetadataValue(BetterBedrockMenus.getInstance(), "button "+ button));

                    if(type.isRightClick())
                    {
                        player.setMetadata("setting-type", new FixedMetadataValue(BetterBedrockMenus.getInstance(), "button-action"));
                        player.sendMessage("§aPlease set a value for §fbutton " + button + " action§a!" );
                        player.sendMessage("§aStart a message with §f!§a if it's a command!");
                    }
                    else{
                        player.setMetadata("setting-type", new FixedMetadataValue(BetterBedrockMenus.getInstance(), "button-type"));
                        player.sendMessage("§aPlease set a value for §fbutton " + button + " name§a!" );


                    }
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
    private void setupMenu(String value, Player player)
    {
        player.setMetadata("setting-value", new FixedMetadataValue(BetterBedrockMenus.getInstance(), value));
        player.sendMessage("§aPlease provide a value for§f " +value.replace("-", " ") + "§a! Type §fCancel§a to cancel and §fReset§a to reset the current value.");
        player.closeInventory();
    }
    private void failure(Player player, String error)
    {

        player.sendMessage("§cThe menu was not completed! Reason:§f " + error + "§c!");
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(player, 9, "§c§lError");
        ItemStack errorBlock = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta errorMeta = errorBlock.getItemMeta();
        errorMeta.setDisplayName("§4§lError");
        errorMeta.setLore(List.of("§cThe menu was not completed!", "§cReason:§f "   + error + "§c!","§aClick to re-open the §fmenu creator§a!"));
        errorBlock.setItemMeta(errorMeta);
        player.setMetadata("menu-error", new FixedMetadataValue(BetterBedrockMenus.getInstance(), true));
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, errorBlock);
        }
        player.openInventory(inventory);
    }
    private void confirm(Player player)
    {
        if(!(player.hasMetadata("file-name")) || !(player.hasMetadata("menu-name")) || !(player.hasMetadata("menu-text")))
        {
            failure(player, "Missing a required field (file name, menu name, and/or menu text)");
            return;
        }
        int buttons = 0;
        if(player.hasMetadata("buttons"))
        {
           buttons  = player.getMetadata("buttons").getFirst().asInt();
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
        StringBuilder command = new StringBuilder("create-menu "+ fileName + " " + menuName + " ^" + menuText);

        for (int i = 1; i <= buttons; i++) {
            try
            {
                command.append(" ^").append(player.getMetadata("button-"+i).getFirst().asString()).append(" ^").append(formatCommand(player.getMetadata("button-action-"+i).getFirst().asString()));
                player.removeMetadata("button-"+i, BetterBedrockMenus.getInstance());
                player.removeMetadata("button-action-"+i, BetterBedrockMenus.getInstance());
            }catch (NoSuchElementException e)
            {
                failure(player, "§cOne or more required values for a button is missing!\n§cPlease verify all set buttons have an §faction");
                return;
            }

        }

        player.performCommand(command.toString());
        player.closeInventory();
        reset(player);
    }
    private void reset(Player player)
    {
        player.removeMetadata("file-name", BetterBedrockMenus.getInstance());
        player.removeMetadata("menu-name", BetterBedrockMenus.getInstance());
        player.removeMetadata("menu-text", BetterBedrockMenus.getInstance());
        for (int i = 1; i < 5; i++) {
            player.removeMetadata("button-"+i, BetterBedrockMenus.getInstance());
            player.removeMetadata("button-action-"+i, BetterBedrockMenus.getInstance());
        }
        player.removeMetadata("opened-menu", BetterBedrockMenus.getInstance());
    }

    private String formatCommand(String input)
    {
        if (input.charAt(0) == '!') {
            StringBuilder inputFormatted = new StringBuilder(input);
            inputFormatted.setCharAt(0, '/');
            return inputFormatted.toString();
        }
        return input;
    }
}