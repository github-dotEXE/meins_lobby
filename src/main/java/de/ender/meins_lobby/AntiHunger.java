package de.ender.meins_lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class AntiHunger implements Listener {
    @EventHandler
    public static void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
