package com.madgique.nomoremapinoffhand.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * Platform-specific helper class using Architectury's @ExpectPlatform
 */
public class PlatformHelper {
    
    /**
     * Get the platform-specific AccessorySlotManager implementation
     * @return AccessorySlotManager instance
     */
    @ExpectPlatform
    public static AccessorySlotManager getAccessoryManager() {
        throw new AssertionError("This method should be implemented by platform-specific code");
    }
}