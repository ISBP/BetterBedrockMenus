# Better Bedrock Menus
![Better Bedrock Menus Banner](https://user-cdn.hackclub-assets.com/019c74f1-7b8e-77d0-a18f-87e532437507/heading_4_.png)

## What is Better Bedrock Menus?
Better Bedrock Menus is a tool to allow people running a Minecraft server with crossplay to be able to create GUIs for bedrock players in a simple fashion!

## Why should I use Better Bedrock Menus?
Better Bedrock Menus offers a much better experience for bedrock players, especially mobile players as not only to Minecraft Java Chest GUIs get misaligned when sent to Bedrock players, but mobile player's can't even hover over them to see the text, resulting in accidentally mis-clicks!
Better Bedrock Menus intends to make the experience much easier for Bedrock players by using the built-in menus for their client!

## How do I install Better Bedrock Menus?
You can download Better Bedrock Menus from the [releases](https://github.com/ISBP/BetterBedrockMenus/releases) tab!
To use it, simply put the .jar file into the plugins folder of any Paper / Spigot server running Ver. 1.21 or later. Please note that you must have Geyser and Floodgate already installed for this plugin to work correctly. You can also install PlaceholderAPI for placeholder support!
## How do I use Better Bedrock Menus?
You can currently create a welcome menu via the config.yml generated within the plugin's data folder (located at /plugins/BetterBedrockMenus/Config.yml) in your server's directory.

You can create menus using the command `/menu-creator`
You can also manually create menus with the command `/create-menu <Menu name> <Menu text>` (Since both the menu name and menu text can be multiple words, 
you must start an argument with ^ to declare it as the body. For example `/create-menu Example Example Menu ^Welcome to the Example Server!` would create a menu in the file Example.json with the name "Example Menu" and the Description "Welcome to Example Server!". Do note that color codes are supported, and you can 
use & in place of the section symbol. 

To send a saved menu, use `/send-menu <menu name> <player>`.

You can also send manual menus with the command `/send-command-menu <Player> <Menu Title> <Menu Body>`, though this is not encouraged.

To delete a saved menu use `/delete-menu <menu name.json>`. Please append the name of the menu with .json .

## Permissions:
The permission "bbm.create" is required to create any menus, so to use the command create-menu or menu-creator.
To send a menu you must have the permission "bbm.send".

### Explanation for non Minecraft players
Minecraft is a sandbox video game that's split into two versions. The original edition, java edition, has the most community servers and is the more well known version, however it only supports PC. Bedrock edition, the later edition, has less community ran servers but does support PC, phone, and *some* consoles. There was no built in crossplay between these versions so the community made a plugin to allow Bedrock players to join Java servers. A lot of Java servers add this plugin, however with Java being a PC only game and Bedrock supporting phones and such, the Java GUIs are really hard to navigate on phones, consoles, and such, so what my plugin does is use allow Java server owners to easily use create GUIs using the system built into bedrock edition.

Example Java Chest GUI:

<img src="https://i.imgur.com/D5mZguG.png" width="300" alt="Bedrock GUI">


Example Bedrock GUI:

<img src="https://i.imgur.com/oKPMFbH.png" width="300" alt="Bedrock GUI">
