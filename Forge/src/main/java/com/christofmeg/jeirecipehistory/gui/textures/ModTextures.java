package com.christofmeg.jeirecipehistory.gui.textures;

import com.christofmeg.jeirecipehistory.Constants;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.resources.ResourceLocation;

public class ModTextures {
    private static ModTextures instance;
    private final SpritesManager spritesManager;
    private final IDrawableStatic buttonIcon;

    public ModTextures(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
        {
            this.buttonIcon = registerGuiSprite();
        }
        instance = this;
    }

    private ResourceLocation registerSprite() {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID, "icon");
        spritesManager.registerSprite(location);
        return location;
    }
    private RenderableSprite registerGuiSprite() {
        ResourceLocation location = registerSprite();
        return new RenderableSprite(spritesManager, location, 16, 16);
    }

    public static ModTextures getInstance() {
        return instance;
    }

    public IDrawableStatic getButtonIcon() {
        return buttonIcon;
    }

}
