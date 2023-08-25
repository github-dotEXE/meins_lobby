package de.ender.meins_lobby;

import de.ender.core.events.PlayerInventoryChangeEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiInventory implements Listener {
    @EventHandler
    public void onInventoryChange(PlayerInventoryChangeEvent event){
        if(event.getPlayer().hasPermission("lobby.inventory")
                && event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
}
