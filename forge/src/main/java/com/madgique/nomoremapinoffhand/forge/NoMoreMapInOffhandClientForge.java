package com.madgique.nomoremapinoffhand.forge;

import com.madgique.nomoremapinoffhand.client.MinimapRenderer;
import com.madgique.nomoremapinoffhand.forge.client.KeyBindings;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class NoMoreMapInOffhandClientForge {

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