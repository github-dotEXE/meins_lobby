package de.ender.meins_lobby;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class AntiDamage implements Listener {
    @EventHandler
    public static void onDamage(EntityDamageByEntityEvent event){
        if((event.getDamager() instanceof Player && event.getDamager().hasPermission("lobby.damage"))
                && ((Player) event.getDamager()).getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
    @EventHandler
    public static void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }
}
