package xyz.pbsi.betterBedrockMenus.Skript;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import xyz.pbsi.betterBedrockMenus.Utils.Menus;

import java.awt.*;

@Name("Send Menu")
@Description("Sends a bedrock menu to the specified player!")
@Since("0.6.0")
public class EffSendMenu extends Effect {

    Expression<Player> player;
    Expression<String> menu;
    public static void register(SyntaxRegistry registry)
    {
        registry.register(SyntaxRegistry.EFFECT, SyntaxInfo.builder(EffSendMenu.class)
                .addPattern("send menu %string% to %player%")
                .supplier(EffSendMenu::new)
                .build()
        );
    }

    @Override
    protected void execute(Event event) {
        if(menu == null || player == null) return;
        Player ePlayer = player.getSingle(event);
        if(ePlayer == null || !ePlayer.isOnline()) return;
        String menuString = menu.getSingle(event);
        Menus.sendMenu(Bukkit.getConsoleSender(), ePlayer, menuString, false);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        menu = (Expression<String>) expressions[0];
        player = (Expression<Player>) expressions[1];
        return true;
    }
}
