package me.alexprogrammerde.pistonmotd.sponge;

import me.alexprogrammerde.pistonmotd.api.PlaceholderParser;
import org.spongepowered.api.Game;

public class CommonPlaceholder implements PlaceholderParser {
    private final Game game;

    protected CommonPlaceholder(Game game) {
        this.game = game;
    }

    @Override
    public String parseString(String text) {
        String returnedString = text;
        returnedString = returnedString.replaceAll("%online%", String.valueOf(game.getServer().getOnlinePlayers().size()));
        returnedString = returnedString.replaceAll("%max%", String.valueOf(game.getServer().getMaxPlayers()));
        returnedString = returnedString.replaceAll("%newline%", "\n");

        return returnedString;
    }
}
