package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.common.gui.overlay.IngredientGrid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = IngredientGrid.class, remap = false)
public interface IngredientGridAccessor {

}
