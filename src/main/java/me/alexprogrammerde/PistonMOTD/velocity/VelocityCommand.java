package me.alexprogrammerde.PistonMOTD.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VelocityCommand implements SimpleCommand {
    private final String[] COMMANDS = { "reload", "help" };
    private final PistonMOTDVelocity plugin;

    protected VelocityCommand(PistonMOTDVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length == 1) {
            switch (invocation.arguments()[0]) {
                case "help":
                    invocation.source().sendMessage(Component.text("Commands:"));
                    invocation.source().sendMessage(Component.text("/pistonmotd help"));
                    invocation.source().sendMessage(Component.text("/pistonmotd reload"));
                  break;
                case "reload":
                    plugin.loadConfig();
                    break;
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.source().hasPermission("pistonmotd.reload") || invocation.source().hasPermission("pistonmotd.help")) {
            List<String> completions = new ArrayList<>();

            if (invocation.arguments().length == 1 && invocation.arguments()[0] != null) {
                for (String string : COMMANDS) {
                    if (string.toLowerCase().startsWith(invocation.arguments()[0].toLowerCase())) {
                        completions.add(string);

                        Collections.sort(completions);
                    }
                }
            }

            return completions;
        }

        return null;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("pistonmotd.admin");
    }
}
