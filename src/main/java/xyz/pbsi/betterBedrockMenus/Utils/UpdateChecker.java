package xyz.pbsi.betterBedrockMenus.Utils;
//Update checker by QWERTZ EXE, used with permission. https://github.com/QWERTZexe/QWERTZ-Core/blob/main/src/main/java/app/qwertz/qwertzcore/util/UpdateChecker.java
//Check out QWERTZCore: https://modrinth.com/plugin/qwertz-core

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {



    public void checkForUpdates() {
        try {
            String latestVersion;
            String currentVersion = BetterBedrockMenus.getInstance().getPluginMeta().getVersion();
            URL url = new URL("https://api.modrinth.com/v2/project/better-bedrock-menus/version");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonArray versions = JsonParser.parseReader(reader).getAsJsonArray();

                if (versions.size() > 0) {
                    JsonObject latestVersionObj = versions.get(0).getAsJsonObject();
                    latestVersion = latestVersionObj.get("version_number").getAsString();

                    if (isNewerVersion(latestVersion, currentVersion)) {
                        BetterBedrockMenus.getInstance().getLogger().warning("A new version of Better Bedrock Menus is available: " + latestVersion);
                        BetterBedrockMenus.getInstance().getLogger().info("Download it at: https://modrinth.com/project/better-bedrock-menus");
                    }
                }
            }
        } catch (Exception e) {
            BetterBedrockMenus.getInstance().getLogger().warning("Failed to check for updates: " + e.getMessage());
        }
    }

    private boolean isNewerVersion(String latestVersion, String currentVersion) {
        String[] latest = latestVersion.split("\\.");
        String[] current = currentVersion.split("\\.");

        for (int i = 0; i < Math.min(latest.length, current.length); i++) {
            int l = Integer.parseInt(latest[i]);
            int c = Integer.parseInt(current[i]);
            if (l > c) return true;
            if (l < c) return false;
        }

        return latest.length > current.length;
    }

}