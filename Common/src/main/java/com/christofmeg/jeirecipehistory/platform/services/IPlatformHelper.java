package com.christofmeg.jeirecipehistory.platform.services;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     * @return The name of the current platform.
     */
    @SuppressWarnings("unused")
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    @SuppressWarnings("unused")
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     * Used in com.christofmeg.jeirecipehistory.platform.ForgePlatformHelper#isDevelopmentEnvironment
     * @return True if in a development environment, false otherwise.
     */
    @SuppressWarnings("unused")
    boolean isDevelopmentEnvironment();

    /**
     * Used in "JEI Recipe History Config" button that opens client-config if Configured is installed
     * @return ForgeConfigHelper configHelper
     */
    IPlatformConfigHelper getConfigHelper();

}
