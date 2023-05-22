package de.ender.meins_lobby.parkour;

import de.ender.core.CConfig;
import de.ender.meins_lobby.Meins_lobby;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CheckpointManager {
    private static final HashMap<Player,Integer> checkpoint = new HashMap<>();

    public static boolean reachedCheckpoint(Player player, Location loc) {
        Meins_lobby plugin = Meins_lobby.getPlugin();
        CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_LOCATIONS, plugin);
        FileConfiguration config = cconfig.getCustomConfig();

        ConfigurationSection cs = config.getConfigurationSection("ParkourCheckpoint."+ ParkourMananger.getCRace(player));
        if(cs == null) {
            return false;
        }
        int CID = getCheckpointIDFromLocation(loc,cs.getValues(false).entrySet());
        int oCID = checkpoint.getOrDefault(player,-1);
        checkpoint.put(player, CID);
        return ((CID != -1) &&(oCID == -1 || (oCID != CID)));
    }
    public static void resetCheckpoint(Player player) {
        checkpoint.put(player, -1);
    }
    public static Location getCCheckpointLocation(Player player) {
        Meins_lobby plugin = Meins_lobby.getPlugin();
        CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_LOCATIONS, plugin);
        FileConfiguration config = cconfig.getCustomConfig();
        Location loc = config.getLocation("ParkourCheckpoint."+ ParkourMananger.getCRace(player)+"."+checkpoint.get(player));
        if(loc == null) {
            loc = config.getLocation("ParkourStart."+ ParkourMananger.getCRace(player));
        }
        loc.setYaw(player.getLocation().getYaw());
        loc.setPitch(player.getLocation().getPitch());
        return loc.add(0.5,0,0.5);
    }

    private static int getCheckpointIDFromLocation(Location loc, Set<Map.Entry<String, Object>> entySet){
        int r = -1;
        for (Map.Entry<String, Object> entry : entySet) {
            if (Objects.equals(loc, entry.getValue())){
                r = Integer.parseInt(entry.getKey());
                break;
            }
        }
        return r;
    }
}
