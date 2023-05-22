package de.ender.meins_lobby.parkour;

import de.ender.core.CConfig;
import de.ender.meins_lobby.Meins_lobby;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ParkourItemListener implements Listener {
    @EventHandler
    public static void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();
        String name = null;
        if (item != null&&item.getItemMeta()!=null) name = item.getItemMeta().getDisplayName();
        if((action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) || name == null||name.isEmpty() || !ParkourMananger.inRace(player)) return;
        Location loc;
        String race = ParkourMananger.getCRace(player);
        switch (name) {
            case "§b§k-§b§l LastCheckpoint §b§k-":
                loc = CheckpointManager.getCCheckpointLocation(player);
                player.teleport(loc);
                event.setCancelled(true);
                break;
            case "§b§k-§b§l ToStart §b§k-":
                Meins_lobby plugin = Meins_lobby.getPlugin();
                CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_LOCATIONS, plugin);
                FileConfiguration config = cconfig.getCustomConfig();
                loc = config.getLocation("ParkourStart."+ ParkourMananger.getCRace(player));
                if (loc != null) {
                    player.teleport(loc);
                }
                event.setCancelled(true);
                break;
            case "§b§k-§b§l CancelRace §b§k-":

                ParkourMananger.cancelRace(player);
                player.sendActionBar(ChatColor.RED+"Cancelled race "+race+"!");
                event.setCancelled(true);
                break;
        }
    }
    @EventHandler
    public static void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        String name = item.getItemMeta().getDisplayName();

        if (Objects.equals(name, "§b§k-§b§l LastCheckpoint §b§k-") ||
                Objects.equals(name, "§b§k-§b§l ToStart §b§k-") ||
                Objects.equals(name, "§b§k-§b§l CancelRace §b§k-")) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        if(ParkourMananger.inRace(event.getPlayer())) ParkourMananger.cancelRace(event.getPlayer());
    }
    @EventHandler
    public static void onInventoryInteraction(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if(item ==null)return;
        ItemMeta itemmeta = item.getItemMeta();
        if(itemmeta == null) return;
        String name = itemmeta.getDisplayName();

        if (Objects.equals(name, "§b§k-§b§l LastCheckpoint §b§k-") ||
                Objects.equals(name, "§b§k-§b§l ToStart §b§k-") ||
                Objects.equals(name, "§b§k-§b§l CancelRace §b§k-")) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }
}
