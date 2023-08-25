package de.ender.meins_lobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiskPlayer implements Listener {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    @EventHandler
    public static void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity e = event.getRightClicked();

        if(!(e instanceof ItemFrame)) return;
        ItemFrame itemFrame = (ItemFrame) e;
        ItemStack item = itemFrame.getItem();
        if(item.getType().isRecord()) {
            player.stopSound(SoundCategory.RECORDS);

            if(item.getItemMeta()!=null&&item.getItemMeta().displayName()!=null)
                player.sendActionBar(miniMessage.deserialize("<green>Now playing:</green> ").append(item.getItemMeta().displayName()));
            else player.sendActionBar(miniMessage.deserialize("<green>Now playing:</green> <gray>"+
                    item.getType().name().replaceAll("MUSIC_DISC_","").toLowerCase()));

            if(!player.isSneaking()) player.playSound(e.getLocation(),
                    Sound.valueOf(item.getType().name()), SoundCategory.RECORDS, 1, 1);
            else player.playSound(player,
                    Sound.valueOf(item.getType().name()), SoundCategory.RECORDS, 1, 1);

            event.setCancelled(true);
        }
    }
    @EventHandler
    public static void onEntityHurt(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        Entity e = event.getEntity();

        if(!(e instanceof ItemFrame && ((ItemFrame) e).getItem().getType().isRecord())) return;
        player.stopSound(SoundCategory.RECORDS);
        //event.setCancelled(true);
    }
}
