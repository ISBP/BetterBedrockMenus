package xyz.pbsi.betterBedrockMenus.Utils;

import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;
import java.util.function.Consumer;

public class SkriptUtils {

    public static void registerModules(SyntaxRegistry registry, Consumer<SyntaxRegistry>... consumers) {
        Arrays.stream(consumers).forEach(consumer -> consumer.accept(registry));
    }
}
