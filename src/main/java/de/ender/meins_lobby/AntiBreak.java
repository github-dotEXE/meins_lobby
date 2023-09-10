package de.ender.meins_lobby;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class AntiBreak implements Listener {
    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event){
        if (event.getPlayer().hasPermission("lobby.breakblocks") &&
                event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
}
