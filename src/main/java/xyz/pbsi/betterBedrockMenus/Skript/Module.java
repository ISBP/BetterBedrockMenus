package xyz.pbsi.betterBedrockMenus.Skript;

import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import xyz.pbsi.betterBedrockMenus.BetterBedrockMenus;
import xyz.pbsi.betterBedrockMenus.Utils.SkriptUtils;

public class Module implements AddonModule {
    @Override
    public void init(SkriptAddon addon) {
        AddonModule.super.init(addon);
    }

    @Override
    public void load(SkriptAddon addon) {
        BetterBedrockMenus.getInstance().getLogger().info("Loaded the Skript addon");
        SkriptUtils.registerModules(addon.syntaxRegistry(),
                EffSendMenu::register);
    }

    @Override
    public String name() {
        return "";
    }

}
