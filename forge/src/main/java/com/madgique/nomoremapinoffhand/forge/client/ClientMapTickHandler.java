package com.madgique.nomoremapinoffhand.forge.client;

import com.madgique.nomoremapinoffhand.NoMoreMapInOffhandMod;
import com.madgique.nomoremapinoffhand.platform.PlatformHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NoMoreMapInOffhandMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class ClientMapTickHandler {


    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Phase END uniquement
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        
        if (player == null || minecraft.level == null) {
            return;
        }


        // Get maps from accessory slots and update them on client side
        var maps = PlatformHelper.getAccessoryManager().getMapsFromSlots(player);
        for (ItemStack mapStack : maps) {
            if (!mapStack.isEmpty() && mapStack.getItem() instanceof MapItem) {
                processMapStackClient(player, mapStack);
            }
        }
    }

    private static void processMapStackClient(LocalPlayer player, ItemStack stack) {
        // Récupérer le mapId
        Integer mapId = MapItem.getMapId(stack);
        if (mapId == null) return;

        // Récupérer le MapItemSavedData
        MapItemSavedData data = MapItem.getSavedData(mapId, player.level());
        if (data == null) return;

        // Vérifier si le joueur est dans la bonne dimension pour cette map
        if (data.dimension != player.level().dimension()) {
            return; // Le joueur n'est pas dans la dimension de cette map
        }

        // Force update on client side
        MapItem mapItem = (MapItem) stack.getItem();
        mapItem.update(player.level(), player, data);
        
        // Update player position on the map
        data.tickCarriedBy(player, stack);
    }
}