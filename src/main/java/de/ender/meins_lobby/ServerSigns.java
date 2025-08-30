package de.ender.meins_lobby;

import de.ender.core.PluginMessageManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.sign.Side;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerSigns implements Listener, CommandExecutor, TabCompleter {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            BlockState blockstate = block.getState();
            if (action == Action.RIGHT_CLICK_BLOCK && blockstate instanceof Sign sign) {
                if(!sign.getLine(0).contains(ChatColor.AQUA+"[Server]") ||
                        !PluginMessageManager.getServers().contains(sign.getLine(1))) return;

                player.sendMessage(miniMessage.deserialize("<aqua>Trying to connect you to <dark_purple>"+sign.getLine(1) +"</dark_purple>!"));
                PluginMessageManager.connectSafely(player.getName(),sign.getLine(1));
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockState blockstate = block.getState();
        if (blockstate instanceof Sign) {
            Sign sign = (Sign) blockstate;
            if(!sign.getLine(0).contains(ChatColor.AQUA+"[Server]")) return;
            if(!player.hasPermission("lobby.break.serversign")) event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = ((Player) sender);
        Block block = player.getTargetBlockExact(10);
        BlockFace bf = player.getTargetBlockFace(10);
        if(block== null || bf == null){
            return false;
        }
        if(!player.hasPermission("lobby.serversign")) {
            return false;
        }
        Vector v = bf.getDirection();
        int x = block.getX() + v.getBlockX();
        int y = block.getY() + v.getBlockY();
        int z = block.getZ() + v.getBlockZ();
        //player.sendMessage("x"+x+"y"+y+"z"+z+"v"+v);
        Location loc = new Location(player.getWorld(), x, y, z);
        Block b = loc.getBlock();
        b.setType(Material.OAK_WALL_SIGN);
        BlockState bs = b.getState();

        Directional dir = (Directional) bs.getBlockData();
        dir.setFacing(bf);
        bs.setBlockData(dir);
        bs.update();

        Sign sign = (Sign) bs;
        sign.setLine(0,ChatColor.AQUA+"[Server]");
        sign.setLine(1,args[0]);
        if(args.length == 2) sign.line(3,miniMessage.deserialize(args[1]));

        sign.update();

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completes = new ArrayList<>();
        if(args.length == 1) {
            commands.addAll(PluginMessageManager.getServers());
        }
        StringUtil.copyPartialMatches(args[args.length-1], commands,completes); //copy matches of first argument
        Collections.sort(commands);//sort the list
        return commands;
    }
}
