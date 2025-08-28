package com.madgique.nomoremapinoffhand.map;

import com.madgique.nomoremapinoffhand.platform.PlatformHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;

import java.util.List;

/**
 * Common logic for updating maps in accessory slots
 */
public class MapUpdateManager {
    
    /**
     * Update all maps in accessory slots for a player
     * @param player The player whose maps to update
     */
    public static void updateMapsForPlayer(Player player) {
        Level level = player.level();
        if (level == null) return;
        
        List<ItemStack> maps = PlatformHelper.getAccessoryManager().getMapsFromSlots(player);
        
        for (ItemStack mapStack : maps) {
            if (mapStack.isEmpty() || !(mapStack.getItem() instanceof MapItem)) {
                continue;
            }
            
            updateMapStack(player, mapStack, level);
        }
    }
    
    /**
     * Update a specific map stack
     * @param player The player carrying the map
     * @param mapStack The map ItemStack
     * @param level The world level
     */
    public static void updateMapStack(Player player, ItemStack mapStack, Level level) {
        MapItem mapItem = (MapItem) mapStack.getItem();
        Integer mapId = MapItem.getMapId(mapStack);
        
        if (mapId == null) return;
        
        MapItemSavedData mapData = MapItem.getSavedData(mapId, level);
        if (mapData == null) return;
        
        // Check if player is in the right dimension for this map
        if (mapData.dimension != level.dimension()) {
            return;
        }
        
        // Update the map data
        mapItem.update(level, player, mapData);
        
        // Update player position on the map
        mapData.tickCarriedBy(player, mapStack);
        
        // Force map synchronization - this was the missing piece!
        mapData.setDirty();
        
        // Send map update packet to client if this is a server player
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Force synchronization by sending update packet if available
            var packet = mapData.getUpdatePacket(mapId, serverPlayer);
            if (packet != null) {
                serverPlayer.connection.send(packet);
            }
        }
    }
    
    /**
     * Get the first map from accessory slots for rendering
     * @param player The player to check
     * @return ItemStack containing a map, or ItemStack.EMPTY if none found
     */
    public static ItemStack getMapForRendering(Player player) {
        return PlatformHelper.getAccessoryManager().getFirstMapFromSlots(player);
    }
}