package com.madgique.mapcuriosslot.forge;

import com.madgique.mapcuriosslot.forge.client.KeyBindings;
import com.madgique.mapcuriosslot.forge.client.MinimapRenderer;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class MapCuriosSlotClientForge {

    public static void init() {
        // Register minimap overlay renderer
        ClientGuiEvent.RENDER_HUD.register((guiGraphics, partialTick) -> {
            MinimapRenderer.renderMinimap(guiGraphics, partialTick);
        });
        
        // Register key bindings on mod event bus (for RegisterKeyMappingsEvent)
        // and input handlers on forge event bus (for InputEvent.Key)
        FMLJavaModLoadingContext.get().getModEventBus().addListener(KeyBindings::registerKeyMappings);
        MinecraftForge.EVENT_BUS.addListener(KeyBindings::onKeyInput);
    }
}