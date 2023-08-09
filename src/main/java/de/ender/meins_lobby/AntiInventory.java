package de.ender.meins_lobby;

import de.ender.core.events.PlayerInventoryChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiInventory implements Listener {
    @EventHandler
    public void onInventoryChange(PlayerInventoryChangeEvent event){
        if(event.getPlayer().hasPermission("lobby.inventory")) return;
        event.setCancelled(true);
    }
}
