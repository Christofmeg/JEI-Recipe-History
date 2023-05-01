package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.gui.recipes.IRecipeGuiLogic;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.recipes.RecipeTransferManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RecipesGui.class, remap = false)
public interface RecipesGuiAccessor {

    @Accessor
    IRecipeGuiLogic getLogic();

    @Accessor
    RecipeTransferManager getRecipeTransferManager();

}
