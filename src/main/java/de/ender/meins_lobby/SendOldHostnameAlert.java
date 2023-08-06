package de.ender.meins_lobby;

import de.ender.core.Log;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.HashSet;
import java.util.Set;

public class SendOldHostnameAlert implements Listener {
    private static Set<Player> playersOnOld = new HashSet<>();
    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        if(event.getHostname().contains("etwas--anders.de")) return;
        Player player = event.getPlayer();
        playersOnOld.add(player);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!playersOnOld.contains(player))return;
        player.sendTitle(ChatColor.GOLD+"mc.etwas--anders.de","neue server adresse",20,200,40);
        player.sendMessage(ChatColor.GOLD+
                "You are using an old address to connect to this server. "+
                "You will soon not be able to connect to the server using it! "+
                "The address you should be using is "+ChatColor.LIGHT_PURPLE+ChatColor.UNDERLINE+"mc.etwas--anders.de"+ChatColor.GOLD+"!");
        player.playSound(player, Sound.BLOCK_BELL_USE,1,1F);
    }

}
