package de.ender.meins_lobby.parkour;

import de.ender.core.CConfig;
import de.ender.core.ItemBuilder;
import de.ender.core.Main;
import de.ender.meins_lobby.Meins_lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ParkourMananger {
    private static final HashMap<Player,Long> startTime = new HashMap<>();
    private static final HashMap<Player,String> currentRace = new HashMap<>();

    public static void startRace(Player player,String raceName) {
        startTime.put(player,System.currentTimeMillis());
        currentRace.put(player,raceName);
        giveItems(player);
    }
    public static void endRace(Player player) {
        Meins_lobby plugin = Meins_lobby.getPlugin();
        CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_TIMES, plugin);
        FileConfiguration config = cconfig.getCustomConfig();

        if(inRace(player)){
            String raceName = getCRace(player);
            long time = timeInCRace(player);
            config.set("LastTime."+player.getUniqueId()+"."+raceName,time);
            long cBestTime = bestTimeInRace(player,raceName);
            if(cBestTime!=0 || cBestTime<=time){
                config.set("BestTime."+player.getUniqueId()+"."+raceName, time);
            }
            cconfig.save();
            currentRace.remove(player);
            startTime.remove(player);
            removeItems(player);
        }
    }
    public static void cancelRace(Player player) {
        if(inRace(player)){
            currentRace.remove(player);
            startTime.remove(player);
            removeItems(player);
        }
    }
    public static boolean inRace(Player player){
        return getCRace(player) != null;
    }
    public static String getCRace(Player player) {
        return currentRace.get(player);
    }
    public static long timeInCRace(Player player) {
        return System.currentTimeMillis()-startTime.get(player);
    }
    public static long bestTimeInRace(Player player, String raceName) {
        Meins_lobby plugin = Meins_lobby.getPlugin();
        CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_TIMES, plugin);
        FileConfiguration config = cconfig.getCustomConfig();

        return config.getLong("BestTime."+player.getUniqueId()+"."+raceName);
    }
    public static long lastTimeInRace(Player player, String raceName) {
        Meins_lobby plugin = Meins_lobby.getPlugin();
        CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_TIMES, plugin);
        FileConfiguration config = cconfig.getCustomConfig();

        return config.getLong("LastTime."+player.getUniqueId()+"."+raceName);
    }
    private static void removeItems(Player player) {
        for(ItemStack entryItem : player.getInventory()){
            if(entryItem != null) {
                if (Objects.equals(entryItem.getItemMeta().getDisplayName(), "§b§k-§b§l LastCheckpoint §b§k-") ||
                        Objects.equals(entryItem.getItemMeta().getDisplayName(), "§b§k-§b§l ToStart §b§k-") ||
                        Objects.equals(entryItem.getItemMeta().getDisplayName(), "§b§k-§b§l CancelRace §b§k-")) {
                    player.getInventory().remove(entryItem);
                }
            }
        }
    }
    private static void giveItems(Player player) {
        player.getInventory().setItem(3,new ItemBuilder(Material.HEAVY_WEIGHTED_PRESSURE_PLATE,1).setName("§b§k-§b§l LastCheckpoint §b§k-").build());
        player.getInventory().setItem(4,new ItemBuilder(Material.LIGHT_WEIGHTED_PRESSURE_PLATE,1).setName("§b§k-§b§l ToStart §b§k-").build());
        player.getInventory().setItem(5,new ItemBuilder(Material.OAK_DOOR,1).setName("§b§k-§b§l CancelRace §b§k-").build());
        player.getInventory().setHeldItemSlot(3);
    }
    public static void start(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : startTime.keySet()) {
                    long timeInRace = timeInCRace(player);
                    player.sendActionBar(ChatColor.GREEN+"Time: "+String.format("%dmin:%ds",
                            TimeUnit.MILLISECONDS.toSeconds(timeInRace)/60,
                            TimeUnit.MILLISECONDS.toSeconds(timeInRace) % 60));
                    if(player.isFlying()){
                        ParkourMananger.cancelRace(player);
                        player.sendMessage(ChatColor.RED+"Cancelled Race! You shouldn't fly while racing!");
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(),20,20);
    }
}
