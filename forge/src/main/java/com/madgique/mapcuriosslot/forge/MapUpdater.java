package com.madgique.mapcuriosslot.forge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import com.madgique.mapcuriosslot.MapCuriosSlotMod;

@Mod.EventBusSubscriber(modid = MapCuriosSlotMod.MOD_ID)
public class MapUpdater {
    
    private static int tickCounter = 0;
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer)) {
            return;
        }
        
        // Update every single tick for maximum responsiveness
        tickCounter++;
        // Pas de filtre sur les ticks pour une réactivité maximale
        
        ServerPlayer player = (ServerPlayer) event.player;
        
        // Get map from Curios slot and update it
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("map").ifPresent(stacksHandler -> {
                for (int i = 0; i < stacksHandler.getSlots(); i++) {
                    ItemStack stack = stacksHandler.getStacks().getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                        MapItem mapItem = (MapItem) stack.getItem();
                        Integer mapId = MapItem.getMapId(stack);
                        if (mapId != null) {
                            MapItemSavedData mapData = MapItem.getSavedData(mapId, player.level());
                            if (mapData != null) {
                                mapItem.update(player.level(), player, mapData);
                                
                                // Send sync packet to client
                                MapSyncPacket packet = new MapSyncPacket(mapId, mapData);
                                NetworkManager.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
                            }
                        }
                    }
                }
            });
        });
    }
}