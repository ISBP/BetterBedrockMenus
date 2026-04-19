package xyz.pbsi.betterBedrockMenus.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TextFormatter {
    //This function replaces the & in messages with §, as you can't type a section symbol within Minecraft java
    //It will only replace it if the following character is not a space, so you can use & normally too

    /**
     * Formats color codes of a string. Will only replace the symbol if the character following the & symbol isn't a space
     * @param message The message to format
     * @return The message with color codes format
     */
    public String formatColorCodes(String message)
    {
        StringBuilder newMessage = new StringBuilder(message);
        for (int i = 0; i < message.length(); i++) {
            if(message.charAt(i) == '&')
            {
                if(!(message.charAt( i + 1) == ' '))
                {
                    newMessage.setCharAt(i, '§');
                }
            }

        }
        return newMessage.toString();
    }

    /**
     * Formats the placeholders in a string from the perspective of a player via Placeholder API
     * @param text The string to be formatted
     * @param player The player who to format from the perspective of
     * @return The string with placeholders formatted
     */
    public String formatPlaceholders(String text, Player player)
    {
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
        {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    /**
     * Formats both color codes and placeholders for a string
     * @param text The text to format
     * @param player The player to format the placeholder's perspective from
     * @return The fully formatted string
     */
    public String fullTextFormat(String text, Player player)
    {
        return formatPlaceholders(formatColorCodes(text), player);
    }
}
