package com.madgique.nomoremapinoffhand.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Platform-agnostic interface for managing accessory/curios slots.
 * Implementations will use either Curios API (Forge) or Accessories API (Fabric).
 */
public interface AccessorySlotManager {
    
    /**
     * Get all maps from "map" type accessory slots
     * @param player The player to check
     * @return List of ItemStacks containing maps
     */
    List<ItemStack> getMapsFromSlots(Player player);
    
    /**
     * Check if player has a map in any accessory slot
     * @param player The player to check
     * @return true if player has a map in accessory slots
     */
    default boolean hasMapInSlots(Player player) {
        return !getMapsFromSlots(player).isEmpty();
    }
    
    /**
     * Get the first map found in accessory slots
     * @param player The player to check
     * @return ItemStack containing a map, or ItemStack.EMPTY if none found
     */
    default ItemStack getFirstMapFromSlots(Player player) {
        List<ItemStack> maps = getMapsFromSlots(player);
        return maps.isEmpty() ? ItemStack.EMPTY : maps.get(0);
    }
}