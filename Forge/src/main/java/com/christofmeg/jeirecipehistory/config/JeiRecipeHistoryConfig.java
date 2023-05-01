package com.christofmeg.jeirecipehistory.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class JeiRecipeHistoryConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ClientConfig CLIENT_CONFIG;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT_CONFIG = specPair.getLeft();
    }

    public final static class ClientConfig {

        private final ForgeConfigSpec.BooleanValue disableAllModFeatures;
        private final ForgeConfigSpec.BooleanValue enableRecipeHistory;
        private final ForgeConfigSpec.BooleanValue shouldMatchNBTTags;
        private final ForgeConfigSpec.IntValue rowCount;
        private final ForgeConfigSpec.IntValue borderTint;

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

    public static boolean isAllModFeatuesDisabled() {
        return CLIENT_CONFIG.disableAllModFeatures.get();

    }
    public static boolean isRecipeHistoryEnabled() {
        return CLIENT_CONFIG.enableRecipeHistory.get();

    }
    public static boolean shouldMatchNBTTags() {
        return CLIENT_CONFIG.shouldMatchNBTTags.get();
    }
    public static int getRowCount() {
        return CLIENT_CONFIG.rowCount.get();
    }
    public static int getBorderTint() {
        return CLIENT_CONFIG.borderTint.get();
    }

}
