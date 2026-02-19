package xyz.pbsi.betterBedrockMenus.Utils;

public class TextFormatter {
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
