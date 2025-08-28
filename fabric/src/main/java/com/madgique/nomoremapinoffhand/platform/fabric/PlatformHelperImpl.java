package com.madgique.nomoremapinoffhand.platform.fabric;

import com.madgique.nomoremapinoffhand.platform.AccessorySlotManager;

/**
 * Fabric-specific implementation of PlatformHelper for Architectury's @ExpectPlatform
 */
public class PlatformHelperImpl {
    
    /**
     * Get the Fabric-specific AccessorySlotManager implementation
     * @return AccessorySlotManager instance using Accessories API
     */
    public static AccessorySlotManager getAccessoryManager() {
        return new FabricAccessorySlotManager();
    }
}