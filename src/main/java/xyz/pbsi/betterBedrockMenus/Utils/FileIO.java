package xyz.pbsi.betterBedrockMenus.Utils;

import com.google.gson.JsonObject;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Callbacks.FileReadCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FileIO {
    public static void writeFileAsync(final FileWriter fileWriter, final JsonObject object, final CommandSender commandSender) throws IOException {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    fileWriter.write(object.toString());
                    fileWriter.close();
                    commandSender.sendMessage("§aCreated file!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BetterBedrockMenus.getInstance());

    }

    public static void readFileAsync(final File file, final FileReadCallback callback) throws IOException {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final HashMap<String, String> hashMap = Json.jsonToHashMap(file);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                             callback.onFileRead(hashMap);
                        }
                    }.runTask(BetterBedrockMenus.getInstance());
                } catch (FileNotFoundException e) {
                    BetterBedrockMenus.getInstance().getLogger().severe(e.getMessage());
                }

            }
        }.runTaskAsynchronously(BetterBedrockMenus.getInstance());
    }
}
