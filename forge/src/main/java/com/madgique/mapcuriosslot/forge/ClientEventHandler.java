package com.madgique.mapcuriosslot.forge;

import com.madgique.mapcuriosslot.MapCuriosSlotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.MapItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = MapCuriosSlotMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    
    private static boolean hasMapInCuriosSlot = false;
    private static int checkCounter = 0;
    private static boolean initialCheckDone = false;
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            initialCheckDone = false;
            return;
        }
        
        // Vérifier régulièrement (toutes les 20 ticks = 1 seconde)
        checkCounter++;
        if (checkCounter >= 20 || !initialCheckDone) {
            checkCounter = 0;
            checkForMapInCuriosSlot();
            initialCheckDone = true;
        }
    }
    
    private static void checkForMapInCuriosSlot() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            // Force a check for map in curios slot
            boolean hasMap = CuriosApi.getCuriosInventory(minecraft.player)
                .map(curiosInventory -> curiosInventory.getStacksHandler("map")
                    .map(stacksHandler -> {
                        for (int i = 0; i < stacksHandler.getSlots(); i++) {
                            var stack = stacksHandler.getStacks().getStackInSlot(i);
                            if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .orElse(false))
                .orElse(false);
            
            hasMapInCuriosSlot = hasMap;
        }
    }
    
    public static boolean hasMapInCuriosSlot() {
        return hasMapInCuriosSlot;
    }
    
    public static void setHasMapInCuriosSlot(boolean hasMap) {
        hasMapInCuriosSlot = hasMap;
    }
}