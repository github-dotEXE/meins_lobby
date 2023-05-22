package de.ender.meins_lobby.parkour;

import de.ender.core.CConfig;
import de.ender.meins_lobby.Meins_lobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ParkourListener implements Listener {
    @EventHandler
    public static void onParkourStart(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock() != null &&
                (event.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE)){
            Meins_lobby plugin = Meins_lobby.getPlugin();
            CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_LOCATIONS, plugin);
            FileConfiguration config = cconfig.getCustomConfig();

            ConfigurationSection cs = config.getConfigurationSection("ParkourStart");

            if(cs != null && cs.getValues(false).containsValue(event.getClickedBlock().getLocation())){ //value is location //key is race name

                String raceName = getRaceNameFromLocation(event.getClickedBlock().getLocation(),cs.getValues(false).entrySet());
                if(Objects.equals(ParkourMananger.getCRace(player),raceName)){
                    ParkourMananger.startRace(player,raceName);
                    CheckpointManager.resetCheckpoint(player);
                    player.sendActionBar(ChatColor.GOLD+"You reset the time for the race!");
                    return;
                }
                if(ParkourMananger.inRace(player)){
                    player.sendActionBar(ChatColor.RED+"You already are in another race right now!");
                    return;
                }
                ParkourMananger.startRace(player,raceName);
                CheckpointManager.reachedCheckpoint(player,event.getClickedBlock().getLocation());
                player.sendActionBar(ChatColor.GREEN+"You started the race!");
            } //else player.sendActionBar(ChatColor.RED+ "Something went wrong!");
        }
    }
    @EventHandler
    public static void onParkourEnd(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock() != null &&
                (event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE)){
            Meins_lobby plugin = Meins_lobby.getPlugin();
            CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_LOCATIONS, plugin);
            FileConfiguration config = cconfig.getCustomConfig();

            ConfigurationSection cs = config.getConfigurationSection("ParkourEnd");

            if(cs!= null && cs.getValues(false).containsValue(event.getClickedBlock().getLocation())){ //value is location //key is race name
                if(!ParkourMananger.inRace(player)){
                    //player.sendActionBar(ChatColor.RED+"You aren't in a Parkour right now!");
                    return;
                }
                String raceName = getRaceNameFromLocation(event.getClickedBlock().getLocation(),cs.getValues(false).entrySet());
                if(!Objects.equals(raceName, ParkourMananger.getCRace(player))){
                    player.sendActionBar(ChatColor.RED+"This isn't the end of your current race!");
                    return;
                }
                ParkourMananger.endRace(player);
                long time = ParkourMananger.lastTimeInRace(player,raceName);
                CheckpointManager.resetCheckpoint(player);
                player.sendActionBar(ChatColor.DARK_GREEN+"You ended the race. Your time is "+ String.format("%dmin:%ds:%dmil",
                        TimeUnit.MILLISECONDS.toSeconds(time)/60,
                        TimeUnit.MILLISECONDS.toSeconds(time) % 60,
                        time % 1000));
            }
        }
    }
    @EventHandler
    public static void onParkourCheckpoint(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock() != null &&
                (event.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE)
                && ParkourMananger.inRace(player)){

            boolean success = CheckpointManager.reachedCheckpoint(player,event.getClickedBlock().getLocation());
            if(!success)return;
            player.sendActionBar(ChatColor.GREEN+"You've reached a checkpoint!");
        }
    }
    private static String getRaceNameFromLocation(Location loc, Set<Map.Entry<String, Object>> entySet){
        for (Map.Entry<String, Object> entry : entySet) {
            if (Objects.equals(loc, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
