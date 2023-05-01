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

public class ForgeConfigHelper implements IPlatformConfigHelper {
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
        return ModList.get()
                .getModContainerById(Constants.MOD_ID)
                .map(ModContainer::getModInfo)
                .flatMap(ConfigGuiHandler::getGuiFactoryFor)
                .map(f -> f.apply(minecraft, minecraft.screen));
    }

    @Override
    public Component getMissingConfigScreenMessage() {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/configured");
        Style style = Style.EMPTY.withUnderlined(true)
                .withClickEvent(clickEvent);
        MutableComponent message = new TranslatableComponent("jei.message.configured");
        return message.setStyle(style);
    }
}
