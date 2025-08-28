package com.madgique.nomoremapinoffhand.platform.forge;

import com.madgique.nomoremapinoffhand.platform.AccessorySlotManager;

/**
 * Forge-specific implementation of PlatformHelper for Architectury's @ExpectPlatform
 */
public class PlatformHelperImpl {
    
    /**
     * Get the Forge-specific AccessorySlotManager implementation
     * @return AccessorySlotManager instance using Curios API
     */
    public static AccessorySlotManager getAccessoryManager() {
        return new ForgeAccessorySlotManager();
    }
}