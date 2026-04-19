package xyz.pbsi.betterBedrockMenus.Utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class Json {
    Gson gson = new Gson();

    /**
     *
     * @param file The file to be converted
     * @return The JSON file as a HashMap
     * @throws FileNotFoundException Occurs when the file provided cannot be found
     */
    public HashMap<String, String> jsonToHashMap(File file) throws FileNotFoundException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        HashMap<String, String> json = gson.fromJson(bufferedReader, HashMap.class);
        return json;
    }
}
