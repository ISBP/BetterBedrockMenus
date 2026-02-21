package xyz.pbsi.betterBedrockMenus.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.betterBedrockMenus.Utils.TextFormatter;

public class MenuSender implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length < 3)
        {
            return false;
        }
        Player targetPlayerJava = Bukkit.getPlayerExact(args[0]);
        if(targetPlayerJava != null && FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId()) != null)
        {
            FloodgatePlayer targetPlayer = FloodgateApi.getInstance().getPlayer(targetPlayerJava.getUniqueId());
            String title = args[1];
            StringBuilder builder = new StringBuilder();
            //Loops through the arguments to accept multiple lengths. Variable "i" is set to 2 to ignore the first 2 arguments.
            for (int i = 2; i < args.length; i++)
            {
                builder.append(args[i]).append(" ");
            }
            String message = builder.toString();
            TextFormatter textFormatter = new TextFormatter();
            title = textFormatter.formatColorCodes(title);
            message = textFormatter.formatColorCodes(message);
            title = PlaceholderAPI.setPlaceholders(targetPlayerJava, title);
            message = PlaceholderAPI.setPlaceholders(targetPlayerJava, message);
            SimpleForm.Builder form = SimpleForm.builder()
                    .title(title)
                    .content(message)
                    .button("Close");
            targetPlayer.sendForm(form);
        }

        else{
            sender.sendMessage("§cThat player is not online!");
        }
        return true;

    }
}
