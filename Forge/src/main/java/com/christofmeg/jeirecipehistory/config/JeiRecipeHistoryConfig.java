package com.christofmeg.jeirecipehistory.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * JeiRecipeHistory configuration class.
 */
public class JeiRecipeHistoryConfig {

    /**
     * The Forge config specification for the client configuration.
     */
    public static final ForgeConfigSpec CLIENT_SPEC;

    /**
     * The client configuration instance.
     */
    public static final ClientConfig CLIENT_CONFIG;

    static {
        // Create the client configuration using the Forge config spec builder
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT_CONFIG = specPair.getLeft();
    }

    /**
     * Inner class representing the client configuration.
     */
    public final static class ClientConfig {

        // Configuration options
        private final ForgeConfigSpec.BooleanValue disableAllModFeatures;
        private final ForgeConfigSpec.BooleanValue enableRecipeHistory;
        private final ForgeConfigSpec.BooleanValue shouldMatchNBTTags;
        private final ForgeConfigSpec.IntValue rowCount;
        private final ForgeConfigSpec.IntValue borderTint;

        /**
         * Constructor for the client configuration.
         *
         * @param builder The Forge config spec builder.
         */
        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("client-settings");
            {
                enableRecipeHistory = builder.comment("Set this to false to toggle Recipe History").define("Show Recipe History", true);
                shouldMatchNBTTags = builder.comment("Match NBT tags when adding items to recipe history").define("Match NBT Tags", true);
                rowCount = builder.comment("Number of rows the recipe history should display items on").defineInRange("Row Count", 2, 1, 5);
                borderTint = builder.comment("Customize the recipe history border tint").defineInRange("Border Tint", 0xee555555, Integer.MIN_VALUE, Integer.MAX_VALUE);
                disableAllModFeatures = builder.comment("Set this to true to disable all JEI Recipe History related features, including the configuration button").define("Toggle All Features", false);
            }
            builder.pop();
        }
    }

    /**
     * Checks if all mod features are disabled.
     *
     * @return True if all mod features are disabled, false otherwise.
     */
    public static boolean isAllModFeatuesDisabled() {
        return CLIENT_CONFIG.disableAllModFeatures.get();

    }

    /**
     * Checks if recipe history is enabled.
     *
     * @return True if recipe history is enabled, false otherwise.
     */
    public static boolean isRecipeHistoryEnabled() {
        return CLIENT_CONFIG.enableRecipeHistory.get();

    }

    /**
     * Checks if NBT tags should be matched when adding items to recipe history.
     *
     * @return True if NBT tags should be matched, false otherwise.
     */
    public static boolean shouldMatchNBTTags() {
        return CLIENT_CONFIG.shouldMatchNBTTags.get();
    }

    /**
     * Gets the number of rows the recipe history should display items on.
     *
     * @return The number of rows.
     */
    public static int getRowCount() {
        return CLIENT_CONFIG.rowCount.get();
    }

    /**
     * Gets the custom border tint for the recipe history.
     *
     * @return The border tint value.
     */
    public static int getBorderTint() {
        return CLIENT_CONFIG.borderTint.get();
    }

}
