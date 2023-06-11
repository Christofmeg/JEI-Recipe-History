package com.christofmeg.jeirecipehistory.recipe;

import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import org.jetbrains.annotations.NotNull;

/**
 * Used to store the location of the recipe in the jei and other information.
 */
public interface IRecipeInfo<R, T> {

    /**
     * The recipe's output, which is only the one the player marked when marking the bookmark, and does not represent all the output.
     */
    T getOutput();

    @NotNull
    IRecipeCategory<R> getRecipeCategory();

    R getRecipe();

    IFocusGroup getFocusGroup();

}
