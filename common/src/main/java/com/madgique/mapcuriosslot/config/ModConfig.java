package com.madgique.mapcuriosslot.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "map_curios_slot")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 50, max = 300)
    public int minimapSize = 150;

    @ConfigEntry.Gui.Tooltip
    public boolean enabled = true;

    @ConfigEntry.Gui.Tooltip  
    public boolean showBorder = true;

    // Position values (not shown in auto-generated GUI, managed by Position Editor)
    public int minimapX = 10;
    public int minimapY = 10;

    // Static helper to get config instance
    public static ModConfig getInstance() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    // Getters for accessing configuration values
    public int getMinimapX() {
        return minimapX;
    }

    public int getMinimapY() {
        return minimapY;
    }

    public int getMinimapSize() {
        return minimapSize;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean shouldShowBorder() {
        return showBorder;
    }

    // Setters for updating configuration values (with auto-save)
    public void setMinimapPosition(int x, int y) {
        this.minimapX = x;
        this.minimapY = y;
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

    public void setMinimapSize(int size) {
        this.minimapSize = size;
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

}