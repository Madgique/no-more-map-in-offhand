package com.madgique.nomoremapinoffhand.forge.client.config;

import com.madgique.nomoremapinoffhand.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModConfigScreen {
    
    public static Screen createConfigScreen(Screen parent) {
        ModConfig config = ModConfig.getInstance();
        
        // Create a hybrid config screen: AutoConfig fields + Position Editor button
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("No More Map in Offhand Configuration"));
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));
        
        // AutoConfig fields manually recreated with same functionality
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enabled"), config.enabled)
            .setDefaultValue(true)
            .setTooltip(Component.literal("Enable or disable the minimap display"))
            .setSaveConsumer(config::setEnabled)
            .build());
            
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show Border"), config.showBorder)
            .setDefaultValue(true)
            .setTooltip(Component.literal("Show decorative border around the minimap"))
            .setSaveConsumer(config::setShowBorder)
            .build());
            
        general.addEntry(entryBuilder.startIntSlider(Component.literal("Minimap Size"), config.minimapSize, 50, 300)
            .setDefaultValue(150)
            .setTooltip(Component.literal("Size of the minimap in pixels"))
            .setSaveConsumer(config::setMinimapSize)
            .build());
        
        // Position section
        general.addEntry(entryBuilder.startTextDescription(Component.literal(""))
            .build()); // Spacer
            
        general.addEntry(entryBuilder.startTextDescription(
            Component.literal("§6Position Configuration"))
            .build());
            
        general.addEntry(entryBuilder.startTextDescription(
            Component.literal("§6Current Position: X=" + config.minimapX + ", Y=" + config.minimapY))
            .build());
            
        general.addEntry(entryBuilder.startTextDescription(
            Component.literal("§7Click the button below to open the visual position editor"))
            .build());
        
        // Position Editor button
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("► Open Position Editor"), false)
            .setDefaultValue(false)
            .setTooltip(Component.literal("Click to open the position configuration screen with drag & drop"))
            .setSaveConsumer(value -> {
                if (value) {
                    Minecraft mc = Minecraft.getInstance();
                    mc.tell(() -> {
                        mc.setScreen(new PositionConfigScreen(parent));
                    });
                }
            })
            .build());
        
        builder.setSavingRunnable(() -> {
            AutoConfig.getConfigHolder(ModConfig.class).save();
        });
        
        return builder.build();
    }
}