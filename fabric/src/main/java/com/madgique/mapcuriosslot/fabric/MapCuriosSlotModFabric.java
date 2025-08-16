package com.madgique.mapcuriosslot.fabric;

import com.madgique.mapcuriosslot.MapCuriosSlotMod;

import net.fabricmc.api.ModInitializer;

public class MapCuriosSlotModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        MapCuriosSlotMod.init();
    }
}