package com.madgique.mapcuriosslot.forge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.client.Minecraft;
import java.util.function.Supplier;

public class MapSyncPacket {
    private final int mapId;
    private final byte[] mapData;
    
    public MapSyncPacket(int mapId, MapItemSavedData data) {
        this.mapId = mapId;
        this.mapData = data.colors.clone(); // Copy the color data
    }
    
    public MapSyncPacket(FriendlyByteBuf buf) {
        this.mapId = buf.readInt();
        int dataLength = buf.readInt();
        this.mapData = new byte[dataLength];
        buf.readBytes(this.mapData);
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(mapId);
        buf.writeInt(mapData.length);
        buf.writeBytes(mapData);
    }
    
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Update client-side map data
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                var clientMapData = minecraft.level.getMapData("map_" + mapId);
                if (clientMapData != null) {
                    System.arraycopy(mapData, 0, clientMapData.colors, 0, Math.min(mapData.length, clientMapData.colors.length));
                    clientMapData.setDirty();
                    
                    // Note: Player decoration update would require different approach for this version
                }
            }
        });
        context.setPacketHandled(true);
    }
}