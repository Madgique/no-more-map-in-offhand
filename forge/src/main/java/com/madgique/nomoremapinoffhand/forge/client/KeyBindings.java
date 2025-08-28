package com.madgique.nomoremapinoffhand.forge.client;

import com.madgique.nomoremapinoffhand.config.ModConfig;
import com.madgique.nomoremapinoffhand.forge.client.config.ModConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyBindings {
    
    public static final KeyMapping OPEN_CONFIG = new KeyMapping(
        "key.no_more_map_in_offhand.config",
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_F8,
        "key.category.no_more_map_in_offhand"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_CONFIG);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (OPEN_CONFIG.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(ModConfigScreen.createConfigScreen(null));
            }
        }
    }
}