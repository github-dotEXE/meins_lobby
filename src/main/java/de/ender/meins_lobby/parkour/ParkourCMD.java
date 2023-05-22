package de.ender.meins_lobby.parkour;

import de.ender.core.CConfig;
import de.ender.meins_lobby.Meins_lobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParkourCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You are not a Player!");
            return false;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("lobby.makeParkour")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return false;
        }

        if(args.length <= 1){
            if(args[0].equals("checkpoint") && ParkourMananger.inRace(player)) {
                Location loc = CheckpointManager.getCCheckpointLocation(player);
                player.teleport(loc);
                return true;
            }
            player.sendMessage(ChatColor.GOLD+"Please use '/parkour start|end|delete|checkpoint racename (id)'");
            return false;
        }

        Meins_lobby plugin = Meins_lobby.getPlugin();
        CConfig cconfig = new CConfig(Meins_lobby.PARKOUR_LOCATIONS, plugin);
        FileConfiguration config = cconfig.getCustomConfig();

        World world =  player.getWorld();
        Location loc = player.getLocation().set(player.getLocation().getBlockX(),player.getLocation().getBlockY(),player.getLocation().getBlockZ());
        loc.setYaw(0);
        loc.setPitch(0);
        switch (args[0]){
            case "start":
                world.getBlockAt(player.getLocation()).setType(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
                config.set("ParkourStart."+args[1],loc);
                cconfig.save();
                break;
            case "end":
                world.getBlockAt(player.getLocation()).setType(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
                config.set("ParkourEnd."+args[1],loc);
                cconfig.save();
                break;
            case "checkpoint":
                if(args.length == 2){
                    player.sendMessage(ChatColor.GOLD+"Please use '/parkour start|end|delete|checkpoint racename (id)'");
                    return false;
                }
                world.getBlockAt(player.getLocation()).setType(Material.STONE_PRESSURE_PLATE);
                config.set("ParkourCheckpoint."+args[1]+"."+args[2],loc);
                cconfig.save();
                break;
            case "delete":
                world.getBlockAt(player.getLocation()).setType(Material.AIR);
                config.set("ParkourEnd."+args[1],null);
                config.set("ParkourStart."+args[1],null);
                cconfig.save();
                break;
        }
        return false;
    }
}
