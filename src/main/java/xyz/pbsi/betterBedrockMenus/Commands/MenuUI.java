package xyz.pbsi.betterBedrockMenus.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class MenuUI implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player player)
        {
            Inventory inventory = Bukkit.createInventory(player, 9, "§3§lMenu Creator");

            ItemStack nameTag = itemWithLore(player, Material.NAME_TAG, "§f§lFile Name", "§aClick to set!", "file-name");
            ItemStack map = itemWithLore(player, Material.MAP, "§f§lMenu Name", "§aClick to set!", "menu-name");
            ItemStack chest = itemWithLore(player, Material.CHEST, "§f§lDescription", "§aClick to set!", "menu-text");
            ItemStack button = itemWithLore(player, Material.OAK_BUTTON, "§f§lButton", "§aLeft click to set name! & §aRight click to set action!", "button-one");
            ItemStack buttonTwo = itemWithLore(player, Material.OAK_BUTTON, "§f§lButton", "§aLeft click to set name! & §aRight click to set action!", "button-two");
            ItemStack emeraldBlock = itemWithLore(player, Material.EMERALD_BLOCK, "§a§lConfirm", "§aClick to confirm!", "N/A");
            ItemStack redstoneBlock = itemWithLore(player, Material.REDSTONE_BLOCK, "§c§lReset", "§cClick to reset!", "N/A");
            inventory.setItem(0, nameTag);
            inventory.setItem(1, map);
            inventory.setItem(2, chest);
            inventory.setItem(3, button);
            inventory.setItem(4, buttonTwo);
            inventory.setItem(5, emeraldBlock);
            inventory.setItem(6, redstoneBlock);
            player.setMetadata("opened-menu", new FixedMetadataValue(BetterBedrockMenus.getInstance(), true));
            player.openInventory(inventory);
         return true;
        }
        return false;
    }
//Returns the item with the lore provided and grabs what the current value is from the player's metadata
    public ItemStack itemWithLore(Player player, Material itemType, String name, String loreText, String metaData)
    {
        ItemStack item = new ItemStack(itemType);
        List<String> lore = new ArrayList<>();
        lore.add(loreText);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if((!metaData.equals("N/A") && !(metaData.equals("button")) && player.hasMetadata(metaData)))
        {
            String loreState = player.getMetadata(metaData).getFirst().asString();
            lore.add("§aValue: §f" + loreState);
        } else if (metaData.equals("button-one")) {
            if(player.hasMetadata("first-button-name"))
            {
                lore.add("§aButton Name: §f" + player.getMetadata("first-button-name").getFirst().asString());
            }
            if(player.hasMetadata("first-button-action"))
            {
                lore.add("§aButton Action: §f" + player.getMetadata("first-button-action").getFirst().asString());

            }
        } else if (metaData.equals("button-two"))
        {
            if(player.hasMetadata("second-button-name"))
            {
                lore.add("§aButton Name: §f" + player.getMetadata("second-button-name").getFirst().asString());
            }
            if(player.hasMetadata("second-button-action"))
            {
                lore.add("§aButton Action: §f" + player.getMetadata("second-button-action").getFirst().asString());

            }
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
