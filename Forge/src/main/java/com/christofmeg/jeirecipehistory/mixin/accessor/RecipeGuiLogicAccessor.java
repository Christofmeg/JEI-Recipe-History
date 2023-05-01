package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.gui.recipes.RecipeGuiLogic;
import mezz.jei.ingredients.RegisteredIngredients;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RecipeGuiLogic.class, remap = false)
public interface RecipeGuiLogicAccessor {

    @Accessor
    RegisteredIngredients getRegisteredIngredients();

}
