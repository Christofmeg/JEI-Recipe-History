package com.christofmeg.jeirecipehistory.platform;

import com.christofmeg.jeirecipehistory.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

    /**
     * Gets the name of the current platform
     * @return The name of the current platform.
     */
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    /**
     * Checks if a mod with the given id is loaded.
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    /**
     * Check if the game is currently in a development environment.
     * @return True if in a development environment, false otherwise.
     */
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    private final ForgeConfigHelper configHelper = new ForgeConfigHelper();

    @Override
    public ForgeConfigHelper getConfigHelper() {
        return configHelper;
    }

}