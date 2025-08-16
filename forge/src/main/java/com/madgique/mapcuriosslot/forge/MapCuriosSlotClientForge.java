package com.madgique.mapcuriosslot.forge;

import com.madgique.mapcuriosslot.client.MinimapRenderer;
import dev.architectury.event.events.client.ClientGuiEvent;

public class MapCuriosSlotClientForge {

    public static void init() {
        // Register minimap overlay renderer
        ClientGuiEvent.RENDER_HUD.register((guiGraphics, partialTick) -> {
            MinimapRenderer.renderMinimap(guiGraphics, partialTick);
        });
    }
}