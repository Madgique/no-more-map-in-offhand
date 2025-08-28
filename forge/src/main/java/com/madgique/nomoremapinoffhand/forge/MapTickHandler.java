package com.madgique.nomoremapinoffhand.forge;

import com.madgique.nomoremapinoffhand.NoMoreMapInOffhandMod;
import com.madgique.nomoremapinoffhand.map.MapUpdateManager;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NoMoreMapInOffhandMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MapTickHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Phase END uniquement
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        // Côté serveur uniquement
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        // Update maps in accessory slots using common code
        MapUpdateManager.updateMapsForPlayer(player);
    }
}