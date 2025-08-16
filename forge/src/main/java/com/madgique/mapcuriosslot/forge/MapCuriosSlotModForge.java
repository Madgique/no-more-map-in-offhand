package com.madgique.mapcuriosslot.forge;

import com.madgique.mapcuriosslot.MapCuriosSlotClient;
import com.madgique.mapcuriosslot.MapCuriosSlotMod;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MapCuriosSlotMod.MOD_ID)
public class MapCuriosSlotModForge {

  public MapCuriosSlotModForge() {
    // Submit our event bus to let architectury register our content on the right
    // time
    EventBuses.registerModEventBus(MapCuriosSlotMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MapCuriosSlotMod.init();
    
    // Initialize network
    NetworkManager.init();
    
    // set the mod client only
    DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> MapCuriosSlotClient::new);
    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MapCuriosSlotClientForge::init);
  }

}