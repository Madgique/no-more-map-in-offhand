package com.madgique.mapcuriosslot;

import com.madgique.mapcuriosslot.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class MapCuriosSlotMod {

    public static final String MOD_ID = "map_curios_slot";

    public static void init() {
        // Initialize AutoConfig with TOML serializer
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }
}