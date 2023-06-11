package com.christofmeg.jeirecipehistory;

import com.christofmeg.jeirecipehistory.config.JeiRecipeHistoryConfig;
import com.christofmeg.jeirecipehistory.gui.textures.ModTextures;
import com.christofmeg.jeirecipehistory.gui.textures.SpritesManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main mod class for JEI Recipe History.
 */
@Mod(Constants.MOD_ID)
public class JEIRecipeHistory {

    /**
     * Constructor for JEIRecipeHistory.
     */
    public JEIRecipeHistory() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, JeiRecipeHistoryConfig.CLIENT_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterClientReloadListenersEvent);
    }

    /**
     * Event handler for the RegisterClientReloadListenersEvent.
     *
     * @param event The RegisterClientReloadListenersEvent instance.
     */
    @SubscribeEvent
    public void onRegisterClientReloadListenersEvent(RegisterClientReloadListenersEvent event) {
        SpritesManager spritesManager = new SpritesManager(Minecraft.getInstance().textureManager);
        ModTextures textures = new ModTextures(spritesManager);
        event.registerReloadListener(spritesManager);
    }

}