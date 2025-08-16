package com.madgique.mapcuriosslot.forge;

import com.madgique.mapcuriosslot.MapCuriosSlotMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkManager {
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(MapCuriosSlotMod.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    public static void init() {
        int id = 0;
        CHANNEL.registerMessage(id++, MapSyncPacket.class, 
            MapSyncPacket::encode, 
            MapSyncPacket::new, 
            MapSyncPacket::handle);
    }
}