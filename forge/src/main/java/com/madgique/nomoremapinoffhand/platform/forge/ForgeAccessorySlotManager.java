package com.madgique.nomoremapinoffhand.platform.forge;

import com.madgique.nomoremapinoffhand.platform.AccessorySlotManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Forge implementation of AccessorySlotManager using Curios API
 */
public class ForgeAccessorySlotManager implements AccessorySlotManager {
    
    @Override
    public List<ItemStack> getMapsFromSlots(Player player) {
        List<ItemStack> maps = new ArrayList<>();
        
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            // Specifically look for "map" slot type first
            curiosInventory.getStacksHandler("map").ifPresent(stacksHandler -> {
                for (int slot = 0; slot < stacksHandler.getSlots(); slot++) {
                    ItemStack stack = stacksHandler.getStacks().getStackInSlot(slot);
                    if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                        maps.add(stack);
                    }
                }
            });
            
            // Also check all other curio slots for any map items as fallback
            Map<String, ICurioStacksHandler> curios = curiosInventory.getCurios();
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                // Skip "map" slot as we already checked it above
                if ("map".equals(entry.getKey())) {
                    continue;
                }
                
                ICurioStacksHandler stacksHandler = entry.getValue();
                for (int slot = 0; slot < stacksHandler.getSlots(); slot++) {
                    ItemStack stack = stacksHandler.getStacks().getStackInSlot(slot);
                    if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                        maps.add(stack);
                    }
                }
            }
        });
        return maps;
    }
}