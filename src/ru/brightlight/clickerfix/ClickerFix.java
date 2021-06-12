package ru.brightlight.clickerfix;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import java.util.HashMap;
import java.util.Map;

public class ClickerFix extends JavaPlugin {

    public static Map<String, Integer> players = new HashMap<>();
    public static int maxAmountClicksInSec = 10;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(this, () -> players.clear(), 20L, 20L);
        this.packetListener();
    }

    public void packetListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.LOWEST, PacketType.Play.Client.BLOCK_PLACE) {
            public void onPacketReceiving(PacketEvent event) {
                String name = event.getPlayer().getName().toLowerCase();
                if (players.containsKey(name)) {
                    if (players.get(name) > maxAmountClicksInSec) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage("§cКликай медленнее.1");
                    } else {
                        players.put(name, players.get(name) + 1);
                    }
                } else {
                    players.put(name, 1);
                }
            }
        });
    }
}
