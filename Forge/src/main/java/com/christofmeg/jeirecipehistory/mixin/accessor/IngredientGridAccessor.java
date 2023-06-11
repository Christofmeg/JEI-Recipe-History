package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.config.IIngredientGridConfig;
import mezz.jei.gui.overlay.IngredientGrid;
import mezz.jei.gui.overlay.IngredientGridTooltipHelper;
import mezz.jei.render.IngredientListRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Mixin interface for accessing protected members of the IngredientGrid class.
 */
@Mixin(value = IngredientGrid.class, remap = false)
public interface IngredientGridAccessor {

    /**
     * Accessor method for retrieving the ingredient list renderer.
     *
     * @return The ingredient list renderer.
     */
    @Accessor
    IngredientListRenderer getIngredientListRenderer();

    /**
     * Accessor method for retrieving the ingredient grid config.
     *
     * @return The ingredient grid config.
     */
    @Accessor
    IIngredientGridConfig getGridConfig();

    /**
     * Accessor method for retrieving the ingredient grid tooltip helper.
     *
     * @return The ingredient grid tooltip helper.
     */
    @Accessor
    IngredientGridTooltipHelper getTooltipHelper();

    /**
     * Accessor method for setting the area of the ingredient grid.
     *
     * @param area The area of the ingredient grid.
     */
    @Accessor
    void setArea(ImmutableRect2i area);

}
