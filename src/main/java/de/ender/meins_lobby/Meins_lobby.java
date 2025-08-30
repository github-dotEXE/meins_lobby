package de.ender.meins_lobby;

import de.ender.core.Log;
import de.ender.core.UpdateChecker;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Meins_lobby extends JavaPlugin implements Listener {
    public static Meins_lobby plugin;

    @Override
    public void onEnable() {
        Log.enable(this);
        //new UpdateChecker(this,"master").check().downloadLatestMeins();
        plugin = this;

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AntiBreak(),this);
        pluginManager.registerEvents(new AntiHunger(),this);
        pluginManager.registerEvents(new AntiDamage(),this);
        pluginManager.registerEvents(new AntiUse(),this);
        pluginManager.registerEvents(new ServerSigns(),this);
        pluginManager.registerEvents(new DiskPlayer(),this);
        pluginManager.registerEvents(new AntiInventory(),this);
        pluginManager.registerEvents(new AntiXp(),this);
        pluginManager.registerEvents(this,this);

        getCommand("serversign").setExecutor(new ServerSigns());
        getCommand("serversign").setTabCompleter(new ServerSigns());
        getCommand("getplayers").setExecutor(new GetPlayersCMD());
        getCommand("getplayers").setTabCompleter(new GetPlayersCMD());
    }

    @Override
    public void onDisable() {
        Log.disable(this);
    }
    public static Meins_lobby getPlugin() {
        return plugin;
    }
}
