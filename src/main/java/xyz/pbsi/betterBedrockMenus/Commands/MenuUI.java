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
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class MenuUI implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player player && !(FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())))
        {
            //Creates the inventory
            Inventory inventory = Bukkit.createInventory(player, 9, "§3§lMenu Creator");
            //Calls the item with lore function to generate correct items with the metadata pre-applied.
            ItemStack nameTag = itemWithLore(player, Material.NAME_TAG, "§f§lFile Name", "§aClick to set!", "file-name");
            ItemStack map = itemWithLore(player, Material.MAP, "§f§lMenu Name", "§aClick to set!", "menu-name");
            ItemStack chest = itemWithLore(player, Material.CHEST, "§f§lDescription", "§aClick to set!", "menu-text");
            ItemStack button = itemWithLore(player, Material.OAK_BUTTON, "§f§lButton One", "§aLeft click to set name! & §aRight click to set action!", "button-1");
            ItemStack buttonTwo = itemWithLore(player, Material.OAK_BUTTON, "§f§lButton Two", "§aLeft click to set name! & §aRight click to set action!", "button-2");
            ItemStack buttonThree = itemWithLore(player, Material.OAK_BUTTON, "§f§lButton Three", "§aLeft click to set name! & §aRight click to set action!", "button-3");
            ItemStack buttonFour = itemWithLore(player, Material.OAK_BUTTON, "§f§lButton Four", "§aLeft click to set name! & §aRight click to set action!", "button-4");
            ItemStack emeraldBlock = itemWithLore(player, Material.EMERALD_BLOCK, "§a§lConfirm", "§aClick to confirm!", "N/A");
            ItemStack redstoneBlock = itemWithLore(player, Material.REDSTONE_BLOCK, "§c§lReset", "§cClick to reset!", "N/A");
            inventory.setItem(0, nameTag);
            inventory.setItem(1, map);
            inventory.setItem(2, chest);
            inventory.setItem(3, button);
            inventory.setItem(4, buttonTwo);
            inventory.setItem(5, buttonThree);
            inventory.setItem(6, buttonFour);
            inventory.setItem(7, emeraldBlock);
            inventory.setItem(8, redstoneBlock);
            player.setMetadata("opened-menu", new FixedMetadataValue(BetterBedrockMenus.getInstance(), true));
            player.openInventory(inventory);
         return true;
        }
        if(sender instanceof Player javaPlayer && (FloodgateApi.getInstance().isFloodgatePlayer(javaPlayer.getUniqueId())))
        {
            FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(javaPlayer.getUniqueId());
            CustomForm.Builder form = CustomForm.builder()
                    .title("Menu Creator")
                    .label(
                            "§aPlease type out a value for each section of the menu! You can avoid setting the buttons if you don't want to add any by §fleaving them blank§a."
                    )
                    .input("§3File Name")
                    .input("§3Menu Title")
                    .input("§3Description")
                    .input("§3First Button Name")
                    .input("§3First Button Action")
                    .input("§3Second Button Name")
                    .input("§3Second Button Action")
                    .validResultHandler(response -> {
                        try{
                            if(response.getInput(7) != null)
                            {
                                javaPlayer.performCommand("create-menu " + Objects.requireNonNull(response.getInput(1)).replace(" ", "-") + " "
                                        + response.getInput(2) + " ^"
                                        + response.getInput(3) + " ^"
                                        + response.getInput(4) + " ^"
                                        + response.getInput(5) + " ^"
                                        + response.getInput(6) + " ^"
                                        + response.getInput(7)
                                );
                            } else if (response.getInput(6 )!= null) {
                                javaPlayer.performCommand("create-menu " + Objects.requireNonNull(response.getInput(1)).replace(" ", "-") + " "
                                        + response.getInput(2) + " ^"
                                        + response.getInput(3) + " ^"
                                        + response.getInput(4) + " ^"
                                        + response.getInput(5)

                                );
                            } else if (response.getInput(3) != null) {
                                javaPlayer.performCommand("create-menu " + Objects.requireNonNull(response.getInput(1)).replace(" ", "-") + " "
                                        + response.getInput(2) + " ^"
                                        + response.getInput(3)
                                );
                            }
                            else {
                                javaPlayer.sendMessage("§cOne or more fields lacked a §frequired input§c! If you are setting a button, you §omust§c set both the name and action!");

                            }
                        }catch(NullPointerException e)
                        {
                            javaPlayer.sendMessage("§cOne or more fields lacked a §frequired input§c! If you are setting a button, you §omust§c set both the name and action!");
                        }


                    });
            player.sendForm(form);
            return true;

        }
        sender.sendMessage("This command can only be used as a player!");
        return true;
    }
//Returns the item with the lore provided and grabs what the current value is from the player's metadata
    private ItemStack itemWithLore(@NotNull Player player, @NotNull Material itemType, @NotNull String name, @NotNull String loreText, @NotNull String metaData)
    {
        //Initializes variables
        ItemStack item = new ItemStack(itemType);
        List<String> lore = new ArrayList<>();
        lore.add(loreText);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        //Checks whether a player has metadata for a specific edit and if so adds it to the lore
        if(((!metaData.equals("N/A") )&& !(metaData.contains("button")) && player.hasMetadata(metaData)))
        {
            String loreState = player.getMetadata(metaData).getFirst().asString();
            lore.add("§aValue: §f" + loreState);
        } else if (metaData.contains("button")) {
            if(player.hasMetadata(metaData))
            {
                lore.add("§aButton Name: §f" + player.getMetadata(metaData).getFirst().asString());
            }
            if(player.hasMetadata(metaData.replace("button-", "button-action-")))
            {
                lore.add("§aButton Action: §f" + player.getMetadata(metaData.replace("button-", "button-action-")).getFirst().asString());
            }
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
