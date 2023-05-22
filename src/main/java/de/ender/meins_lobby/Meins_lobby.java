package de.ender.meins_lobby;

import de.ender.core.Log;
import de.ender.meins_lobby.parkour.ParkourCMD;
import de.ender.meins_lobby.parkour.ParkourItemListener;
import de.ender.meins_lobby.parkour.ParkourListener;
import de.ender.meins_lobby.parkour.ParkourMananger;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Meins_lobby extends JavaPlugin implements Listener {
    public static final String PARKOUR_TIMES = "ParkourTimes";
    public static final String PARKOUR_LOCATIONS = "ParkourLocations";
    public static Meins_lobby plugin;

    @Override
    public void onEnable() {
        Log.log(ChatColor.AQUA + "Enabling meins_lobby...");

        plugin = this;

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AntiBreak(),this);
        pluginManager.registerEvents(new AntiHunger(),this);
        pluginManager.registerEvents(new AntiDamage(),this);
        pluginManager.registerEvents(new ServerSigns(),this);
        pluginManager.registerEvents(new DiskPlayer(),this);
        pluginManager.registerEvents(new ParkourListener(),this);
        pluginManager.registerEvents(new ParkourItemListener(),this);
        pluginManager.registerEvents(this,this);

        getCommand("serversign").setExecutor(new ServerSigns());
        getCommand("serversign").setTabCompleter(new ServerSigns());
        getCommand("parkour").setExecutor(new ParkourCMD());
        getCommand("getplayers").setExecutor(new GetPlayersCMD());
        getCommand("getplayers").setTabCompleter(new GetPlayersCMD());

        ParkourMananger.start();
    }

    @Override
    public void onDisable() {
        Log.log(ChatColor.AQUA + "Disabling meins_lobby...");
    }
    public static Meins_lobby getPlugin() {
        return plugin;
    }
}
