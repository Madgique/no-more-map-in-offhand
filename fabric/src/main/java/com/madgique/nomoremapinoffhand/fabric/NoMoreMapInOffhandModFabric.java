package com.madgique.nomoremapinoffhand.fabric;

import com.madgique.nomoremapinoffhand.NoMoreMapInOffhandMod;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.slot.SlotTypeReference;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.ModInitializer;

public class NoMoreMapInOffhandModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        NoMoreMapInOffhandMod.init();
        
        // The slot registration is handled by datapack files in resources/data/accessories/
    }
}