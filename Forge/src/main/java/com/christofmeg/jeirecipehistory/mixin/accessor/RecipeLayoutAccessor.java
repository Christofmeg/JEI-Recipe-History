package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.gui.ingredients.RecipeSlots;
import mezz.jei.gui.recipes.RecipeLayout;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RecipeLayout.class, remap = false)
public interface RecipeLayoutAccessor {

    @Accessor("ingredientCycleOffset")
    int getOffset();

    @Accessor("LOGGER")
    Logger getLogger();

    @Mutable
    @Accessor("recipeSlots")
    void setRecipeSlots(RecipeSlots recipeSlots);

}
