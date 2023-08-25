package de.ender.meins_lobby;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DiskPlayer implements Listener {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    @EventHandler
    public static void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity e = event.getRightClicked();
        if(e instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) e;
            ItemStack item = itemFrame.getItem();
            if(item.getType().name().contains("DISC")) {
                //player.stopAllSounds();
                player.stopSound(SoundCategory.RECORDS);
                player.sendActionBar(miniMessage.deserialize("<rainbow>Now playing: "+ item.getType().name()));

                if(!player.isSneaking()) player.playSound(e.getLocation(),
                        Sound.valueOf(item.getType().name()), SoundCategory.RECORDS, 1, 1);
                else player.playSound(player,
                        Sound.valueOf(item.getType().name()), SoundCategory.RECORDS, 1, 1);

                event.setCancelled(true);
            }
        }
    }
}
