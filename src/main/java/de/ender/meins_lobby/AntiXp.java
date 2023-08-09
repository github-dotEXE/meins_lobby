package de.ender.meins_lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class AntiXp implements Listener {
    @EventHandler
    public void onXPChange(PlayerExpChangeEvent event){
        event.setAmount(0);
    }
}
