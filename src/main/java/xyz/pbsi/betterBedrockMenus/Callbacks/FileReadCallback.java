package xyz.pbsi.betterBedrockMenus.Callbacks;

import org.bukkit.command.CommandSender;

import java.util.HashMap;

public interface FileReadCallback {
    void onFileRead(HashMap<String, String> hashMap);
}
