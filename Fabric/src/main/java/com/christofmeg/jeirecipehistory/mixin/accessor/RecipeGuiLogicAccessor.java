package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.common.gui.recipes.RecipeGuiLogic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = RecipeGuiLogic.class, remap = false)
public interface RecipeGuiLogicAccessor {

}
