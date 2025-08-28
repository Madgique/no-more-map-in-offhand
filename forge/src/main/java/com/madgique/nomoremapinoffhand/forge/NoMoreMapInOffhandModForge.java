package com.madgique.nomoremapinoffhand.forge;

import com.madgique.nomoremapinoffhand.NoMoreMapInOffhandMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NoMoreMapInOffhandMod.MOD_ID)
public class NoMoreMapInOffhandModForge {

  public NoMoreMapInOffhandModForge() {
    // Submit our event bus to let architectury register our content on the right
    // time
    EventBuses.registerModEventBus(NoMoreMapInOffhandMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    NoMoreMapInOffhandMod.init();

    // Initialize client-side features
    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> NoMoreMapInOffhandClientForge::init);
    
    // Register config screen for Forge (prevents grayed out config button)
    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClothConfigForge::registerModsPage);
    
    // Key bindings are registered from NoMoreMapInOffhandClientForge.init() instead
  }

}