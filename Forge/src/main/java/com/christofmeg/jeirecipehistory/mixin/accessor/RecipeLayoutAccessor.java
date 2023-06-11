package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.gui.ingredients.RecipeSlots;
import mezz.jei.gui.recipes.RecipeLayout;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor interface for RecipeLayout mixin.
 */
@Mixin(value = RecipeLayout.class, remap = false)
public interface RecipeLayoutAccessor {

    /**
     * Accessor method for retrieving the ingredient cycle offset.
     *
     * @return The ingredient cycle offset.
     */
    @Accessor("ingredientCycleOffset")
    int getOffset();

    /**
     * Accessor method for retrieving the logger instance.
     *
     * @return The logger instance.
     */
    @Accessor("LOGGER")
    Logger getLogger();

    /**
     * Mutator method for setting the recipe slots.
     *
     * @param recipeSlots The recipe slots to set.
     */
    @Mutable
    @Accessor("recipeSlots")
    void setRecipeSlots(RecipeSlots recipeSlots);

}
