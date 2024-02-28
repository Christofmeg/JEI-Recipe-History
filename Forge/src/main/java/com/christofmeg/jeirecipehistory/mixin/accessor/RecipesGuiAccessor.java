package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.api.runtime.IRecipesGui;
import mezz.jei.gui.recipes.IRecipeGuiLogic;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.recipes.RecipeTransferManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Mixin interface for accessing protected members of the RecipesGui class.
 */
@Mixin(value = IRecipesGui.class, remap = false)
public interface RecipesGuiAccessor {

    /**
     * Accessor method for retrieving the recipe gui logic.
     *
     * @return The recipe gui logic.
     */
    @Accessor
    IRecipeGuiLogic getLogic();

    /**
     * Accessor method for retrieving the recipe transfer manager.
     *
     * @return The recipe transfer manager.
     */
    @Accessor
    RecipeTransferManager getRecipeTransferManager();

}

