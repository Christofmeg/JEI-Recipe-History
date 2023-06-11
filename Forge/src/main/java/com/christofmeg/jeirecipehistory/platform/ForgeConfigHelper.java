package com.christofmeg.jeirecipehistory.platform;

import com.christofmeg.jeirecipehistory.Constants;
import com.christofmeg.jeirecipehistory.platform.services.IPlatformConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

/**
 * Implementation of the {@link IPlatformConfigHelper} interface specific to the Forge platform.
 * This class provides configuration-related functionality and utilities for Forge.
 */
public class ForgeConfigHelper implements IPlatformConfigHelper {

    /**
     * Retrieves the configuration screen for the mod.
     *
     * @return An optional screen representing the configuration screen for the mod.
     */
    @Override
    public Optional<Screen> getConfigScreen() {
        Minecraft minecraft = Minecraft.getInstance();
/*
 TODO if JEIUtilities releases updated version

         if(ModList.get().isLoaded(Constants.JEI_UTILITIES)) {
             return ModList.get()
                     .getModContainerById(Constants.JEI_UTILITIES)
                     .map(ModContainer::getModInfo)
                     .flatMap(ConfigGuiHandler::getGuiFactoryFor)
                     .map(f -> f.apply(minecraft, minecraft.screen));
         }
         else {
             return ModList.get()
                     .getModContainerById(Constants.MOD_ID)
                     .map(ModContainer::getModInfo)
                     .flatMap(ConfigGuiHandler::getGuiFactoryFor)
                     .map(f -> f.apply(minecraft, minecraft.screen));
         }
*/
        // Otherwise, use the mod's configuration screen.
        return ModList.get()
                .getModContainerById(Constants.MOD_ID)
                .map(ModContainer::getModInfo)
                .flatMap(ConfigGuiHandler::getGuiFactoryFor)
                .map(f -> f.apply(minecraft, minecraft.screen));
    }

    /**
     * Retrieves the component representing the message to display when the configuration screen is missing.
     *
     * @return The component representing the missing configuration screen message.
     */
    @Override
    public Component getMissingConfigScreenMessage() {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/configured");
        Style style = Style.EMPTY.withUnderlined(true)
                .withClickEvent(clickEvent);
        MutableComponent message = new TranslatableComponent("jei.message.configured");
        return message.setStyle(style);
    }
}
