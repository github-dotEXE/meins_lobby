package de.ender.meins_lobby;

import de.ender.core.Log;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiUse implements Listener {
    @EventHandler
    public static void onInteract(PlayerInteractEvent event){
        if(event.getPlayer().hasPermission("lobby.use")
                && event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
    @EventHandler
    public static void onInteractEntity(PlayerInteractEntityEvent event){
        if(event.getPlayer().hasPermission("lobby.use")
                && event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
    @EventHandler
    public static void onArmorstandChange(PlayerArmorStandManipulateEvent event){
        if(event.getPlayer().hasPermission("lobby.use")
                && event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
}
