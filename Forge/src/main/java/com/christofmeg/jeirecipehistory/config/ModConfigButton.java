package com.christofmeg.jeirecipehistory.config;

import com.christofmeg.jeirecipehistory.gui.textures.ModTextures;
import com.christofmeg.jeirecipehistory.platform.services.IPlatformConfigHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.overlay.bookmarks.BookmarkOverlay;
import mezz.jei.input.UserInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ModConfigButton extends GuiIconToggleButton {

    private final BookmarkOverlay bookmarkOverlay;

    public static ModConfigButton create(BookmarkOverlay bookmarkOverlay) {
        IDrawable recipeHistoryButton = ModTextures.getInstance().getButtonIcon();
        return new ModConfigButton(recipeHistoryButton, bookmarkOverlay);
    }

    public ModConfigButton(IDrawable icon, BookmarkOverlay bookmarkOverlay) {
        super(icon, icon);
        this.bookmarkOverlay = bookmarkOverlay;
    }

    @Override
    protected void getTooltips(@NotNull List<Component> tooltip) {
/*
 TODO if JEIUtilities releases updated version

 if(ModList.get().isLoaded(Constants.JEI_UTILITIES)) {
         tooltip.add(Component.nullToEmpty("JEI Utilities Config"));
     }
     else {
         tooltip.add(new TranslatableComponent("jeirecipehistory.tooltip.button"));
     }
*/
        tooltip.add(new TranslatableComponent("jeirecipehistory.tooltip.button"));
    }

    @Override
    protected boolean isIconToggledOn() {
        return false;
    }

    @Override
    protected boolean onMouseClicked(@NotNull UserInput input) {
        if (!JeiRecipeHistoryConfig.isAllModFeatuesDisabled()) {
            if (this.bookmarkOverlay.hasRoom()) {
                if (!input.isSimulate()) {
                    openSettings();
                }
                return true;
            }
        }
        return false;
    }

    private static void openSettings() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        IPlatformConfigHelper configHelper = com.christofmeg.jeirecipehistory.platform.Services.PLATFORM.getConfigHelper();
        Optional<Screen> configScreen = configHelper.getConfigScreen();

        if (configScreen.isPresent()) {
            mc.setScreen(configScreen.get());
        } else {
            Component message = configHelper.getMissingConfigScreenMessage();
            mc.player.displayClientMessage(message, false);
        }
    }

}
