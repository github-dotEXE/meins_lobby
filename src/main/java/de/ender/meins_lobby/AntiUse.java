package de.ender.meins_lobby;

import de.ender.core.Log;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.Console;

public class AntiUse implements Listener {
    @EventHandler
    public static void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        if (block != null) {
            BlockData blockData = block.getBlockData();
            BlockState blockState = block.getState();
            if (blockData instanceof RespawnAnchor respawnAnchorData && action == Action.RIGHT_CLICK_BLOCK) {
                if (item != null && item.getType().equals(Material.GLOWSTONE) &&
                        respawnAnchorData.getCharges() != respawnAnchorData.getMaximumCharges()) // currently this is not a tag (Tag.XXX)
                    return;

                event.setCancelled(true);
            }

            if (blockState instanceof Sign signState) {
                if (signState.getTargetSide(player).lines().stream().anyMatch(line -> line.clickEvent() != null))
                    return;
            }

        }

        if (player.hasPermission("lobby.use") && player.getGameMode().equals(GameMode.CREATIVE))
            return;

        event.setCancelled(true);
    }
    @EventHandler
    public static void onInteractEntity(PlayerInteractEntityEvent event){
        if(event.getPlayer().hasPermission("lobby.use")
                && event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
    @EventHandler
    public static void onArmorstandChange(PlayerArmorStandManipulateEvent event){
        if(event.getPlayer().hasPermission("lobby.use")
                && event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        event.setCancelled(true);
    }
}
