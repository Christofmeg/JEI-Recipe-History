package com.christofmeg.jeirecipehistory.gui.textures;

import com.christofmeg.jeirecipehistory.Constants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Based on {mezz.jei.gui.textures.JeiSpriteUploader}
 */
public class SpritesManager extends TextureAtlasHolder {

    private final Set<ResourceLocation> sprites = new HashSet<>();

    public SpritesManager(TextureManager textureManager) {
        super(textureManager, new ResourceLocation(Constants.MOD_ID, "textures/atlas/gui.png"), "gui");
    }

    public void registerSprite(ResourceLocation location) {
        sprites.add(location);
    }

    @Override
    protected @NotNull Stream<ResourceLocation> getResourcesToLoad() {
        return sprites.stream();
    }

    @Override
    public @NotNull TextureAtlasSprite getSprite(@NotNull ResourceLocation location) {
        return super.getSprite(location);
    }

}
