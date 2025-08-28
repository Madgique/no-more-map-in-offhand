package com.madgique.nomoremapinoffhand.platform.fabric;

import com.madgique.nomoremapinoffhand.platform.AccessorySlotManager;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric implementation of AccessorySlotManager using Accessories API
 */
public class FabricAccessorySlotManager implements AccessorySlotManager {
    
    @Override
    public List<ItemStack> getMapsFromSlots(Player player) {
        List<ItemStack> maps = new ArrayList<>();
        
        var capability = AccessoriesCapability.getOptionally(player);
        if (capability.isEmpty()) {
            return maps;
        }
        
        var accessories = capability.get();
        
        // Check all accessory containers for map items
        accessories.getContainers().forEach((slotType, container) -> {
            var stacks = container.getAccessories();
            for (int slot = 0; slot < stacks.getContainerSize(); slot++) {
                ItemStack stack = stacks.getItem(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                    maps.add(stack);
                }
            }
        });
        
        return maps;
    }
}