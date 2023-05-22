package de.ender.meins_lobby;

import de.ender.core.PluginMessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GetPlayersCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("lobby.getplayers")&&args.length == 1&&(PluginMessageManager.getServers().contains(args[0])||args[0].equals("ALL"))) {;
            PluginMessageManager.playerList(args[0],(playerList) ->{
                PluginMessageManager.playerCount(args[0],(playerCount)->{
                    sender.sendMessage(args[0]+": players("+playerCount+"): "+ Arrays.toString(playerList));
                });
            });
        }
        return false;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completes = new ArrayList<>();
        if(args.length == 1) {
            commands.addAll(PluginMessageManager.getServers());
            commands.add("ALL");
        }
        StringUtil.copyPartialMatches(args[args.length-1], commands,completes); //copy matches of first argument
        Collections.sort(commands);//sort the list
        return commands;
    }
}
