package de.ender.meins_lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiUse implements Listener {
    @EventHandler
    public static void onInteract(PlayerInteractEvent event){
        if(event.getPlayer().hasPermission("lobby.use")) return;
        event.setCancelled(true);
    }
    @EventHandler
    public static void onInteractEntity(PlayerInteractEntityEvent event){
        if(event.getPlayer().hasPermission("lobby.use")) return;
        event.setCancelled(true);
    }
    @EventHandler
    public static void onArmorstandChange(PlayerArmorStandManipulateEvent event){
        if(event.getPlayer().hasPermission("lobby.use")) return;
        event.setCancelled(true);
    }
}
