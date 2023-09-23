package de.ender.meins_lobby;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DiskPlayer implements Listener {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity e = event.getRightClicked();

        if(!(e instanceof ItemFrame)) return;
        ItemFrame itemFrame = (ItemFrame) e;
        ItemStack item = itemFrame.getItem();
        if(item.getType().isRecord()) {
            player.stopSound(SoundCategory.RECORDS);
            stopLoop(player);

            if(!player.isSneaking()) {
                if(item.getItemMeta()!=null&&item.getItemMeta().displayName()!=null)
                    player.sendActionBar(miniMessage.deserialize("<green>Now playing:</green> ").append(item.getItemMeta().displayName()));
                else player.sendActionBar(miniMessage.deserialize("<green>Now playing:</green> <gray>"+
                        item.getType().name().replaceAll("MUSIC_DISC_","").toLowerCase()));

                player.playSound(player,
                        Sound.valueOf(item.getType().name()), SoundCategory.RECORDS, 1, 1);
            } else playLoop(player,Sound.valueOf(item.getType().name()));

            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityHurt(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        Entity e = event.getEntity();

        if(!(e instanceof ItemFrame && ((ItemFrame) e).getItem().getType().isRecord())) return;
        player.stopSound(SoundCategory.RECORDS);
        stopLoop(player);
    }
    @EventHandler
    public void stopLoop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!player.isSneaking()||event.getAction()!= Action.LEFT_CLICK_AIR) return;
        player.stopSound(SoundCategory.RECORDS);
        stopLoop(player);
    }

    private static final HashMap<Sound,Long> discRadio = new HashMap<Sound,Long>() {{
        put(Sound.MUSIC_DISC_CAT,185L);
        put(Sound.MUSIC_DISC_BLOCKS,345L);
        put(Sound.MUSIC_DISC_CHIRP,185L);
        put(Sound.MUSIC_DISC_FAR,174L);
        put(Sound.MUSIC_DISC_MALL,197L);
        put(Sound.MUSIC_DISC_MELLOHI,100L);
        put(Sound.MUSIC_DISC_STAL,150L);
        put(Sound.MUSIC_DISC_STRAD,188L);
        put(Sound.MUSIC_DISC_WAIT,238L);
        put(Sound.MUSIC_DISC_OTHERSIDE,195L);
        put(Sound.MUSIC_DISC_PIGSTEP,148L);
        put(Sound.MUSIC_DISC_RELIC,218L);
    }};
    private static final HashMap<Player, BukkitTask> discTask = new HashMap<>();

    private static void playLoop(Player player,Sound sound){
        player.sendActionBar(miniMessage.deserialize("<green>Radio now playing:</green> <gray>"+
                sound.name().replaceAll("MUSIC_DISC_","").toLowerCase()));
        player.playSound(player, sound, SoundCategory.RECORDS, 1, 1);
        discTask.put(player,new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline()) cancel();
                ArrayList<Sound> possibleSounds = new ArrayList<>( discRadio.keySet());
                possibleSounds.remove(sound);
                playLoop(player, possibleSounds.get( new Random().nextInt(possibleSounds.size()) ) );
            }
        }.runTaskLater(Meins_lobby.getPlugin(),discRadio.get(sound)*20+10));
    }
    private static void stopLoop(Player player){
        if(discTask.get(player)!=null) {
            discTask.get(player).cancel();
            discTask.remove(player);
        }
    }
}
