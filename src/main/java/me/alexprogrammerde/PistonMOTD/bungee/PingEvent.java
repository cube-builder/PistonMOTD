package me.alexprogrammerde.PistonMOTD.bungee;

import me.alexprogrammerde.PistonMOTD.api.PlaceholderUtil;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PingEvent implements Listener {
    final PistonMOTDBungee plugin;
    final File iconFolder;

    public PingEvent(PistonMOTDBungee plugin, File icons) {
        this.plugin = plugin;
        this.iconFolder = icons;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) throws IOException {
        int online;
        int max;
        ServerPing.Players players;
        BaseComponent motd;
        ServerPing.Protocol protocol;
        final String afterIcon = "                                                                            ";
        Favicon icon;
        final Configuration config = plugin.config;

        if (config.getBoolean("overrideonline.activated")) {
            online = config.getInt("overrideonline.value");
        } else {
            online = event.getResponse().getPlayers().getOnline();
        }

        if (config.getBoolean("overridemax.activated")) {
            max = config.getInt("overridemax.value");
        } else {
            max = event.getResponse().getPlayers().getMax();
        }

        if (config.getBoolean("playercounter.activated")) {
            List<ServerPing.PlayerInfo> info = new ArrayList<>();

            int i = 0;
            for (String str : config.getStringList("playercounter.text")) {
                info.add(new ServerPing.PlayerInfo(PlaceholderUtil.parseText(str), String.valueOf(i)));
                i++;
            }

            players = new ServerPing.Players(max, online, info.toArray(new ServerPing.PlayerInfo[config.getStringList("playercounter.text").size()]));
        } else {
            players = new ServerPing.Players(max, online, event.getResponse().getPlayers().getSample());
        }

        if (config.getBoolean("motd.activated")) {
            List<String> list = config.getStringList("motd.text");
            motd = new TextComponent(PlaceholderUtil.parseText(list.get((int) Math.round(Math.random() * (list.size() - 1)))));
        } else {
            motd = event.getResponse().getDescriptionComponent();
        }

        if (config.getBoolean("protocol.activated")) {
            ServerPing.Protocol provided = event.getResponse().getVersion();

            provided.setName(PlaceholderUtil.parseText(config.getString("protocol.text").replaceAll("%aftericon%", afterIcon)));

            protocol = provided;
        } else {
            protocol = event.getResponse().getVersion();
        }

        if (config.getBoolean("icons")) {
            File[] icons = iconFolder.listFiles();

            List<File> validFiles = new ArrayList<>();

            if (icons != null && icons.length != 0) {
                for (File image : icons) {
                    if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                        validFiles.add(image);
                    }
                }

                icon = Favicon.create(ImageIO.read(validFiles.get((int) Math.round(Math.random() * (validFiles.size() - 1)))));
            } else {
                icon = event.getResponse().getFaviconObject();
            }
        } else {
            icon = event.getResponse().getFaviconObject();
        }

        ServerPing ping = new ServerPing(protocol, players, motd, icon);
        event.setResponse(ping);
    }
}
