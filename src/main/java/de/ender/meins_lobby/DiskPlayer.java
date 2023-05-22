package de.ender.meins_lobby;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DiskPlayer implements Listener {
    @EventHandler
    public static void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity e = event.getRightClicked();
        if(e instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) e;
            ItemStack item = itemFrame.getItem();
            if(item.getType().name().toLowerCase().contains("disc")) {
                player.stopAllSounds();
                player.sendActionBar(ChatColor.GREEN+"Now playing "+ item.getType().name());
                //player.getWorld().playSound(player.getLocation(), Sound.valueOf(item.getType().name()), 1, 1);
                player.playSound(e.getLocation(), Sound.valueOf(item.getType().name()), 1, 1);
                event.setCancelled(true);
            }
        }
    }
}
