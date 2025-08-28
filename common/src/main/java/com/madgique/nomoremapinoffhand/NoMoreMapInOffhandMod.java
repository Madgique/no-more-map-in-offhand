package com.madgique.nomoremapinoffhand;

import com.madgique.nomoremapinoffhand.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class NoMoreMapInOffhandMod {

    public static final String MOD_ID = "no_more_map_in_offhand";

    public static void init() {
        // Initialize AutoConfig with TOML serializer
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }
}