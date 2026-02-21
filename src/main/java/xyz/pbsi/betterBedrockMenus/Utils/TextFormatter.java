package xyz.pbsi.betterBedrockMenus.Utils;

public class TextFormatter {
    //This function replaces the & in messages with §, as you can't type a section symbol within Minecraft java
    //It will only replace it if the following character is not a space, so you can use & normally too
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
}
