package com.madgique.nomoremapinoffhand.forge;

import com.madgique.nomoremapinoffhand.forge.client.config.ModConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ClothConfigForge {
    
    public static void registerModsPage() {
        // Register config screen handler to prevent grayed out config button
        ModLoadingContext.get().registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory(
                (minecraft, screen) -> ModConfigScreen.createConfigScreen(screen)
            )
        );
    }
}