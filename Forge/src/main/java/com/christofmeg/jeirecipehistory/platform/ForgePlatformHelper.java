package com.christofmeg.jeirecipehistory.platform;

import com.christofmeg.jeirecipehistory.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

/**
 * Implementation of the {@link IPlatformHelper} interface specific to the Forge platform.
 * This class provides platform-specific functionality and utilities for Forge.
 */
public class ForgePlatformHelper implements IPlatformHelper {

    /**
     * Gets the name of the current platform.
     *
     * @return The name of the current platform.
     */
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    /**
     * Checks if a mod with the given ID is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    /**
     * Checks if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    private final ForgeConfigHelper configHelper = new ForgeConfigHelper();

    /**
     * Gets the Forge configuration helper for accessing mod configuration.
     *
     * @return The Forge configuration helper.
     */
    @Override
    public ForgeConfigHelper getConfigHelper() {
        return configHelper;
    }

}