package com.christofmeg.jeirecipehistory.helper;

import com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.ingredients.TypedIngredient;

/**
 * Helper class for working with ingredients.
 */
public class IngredientHelper {

    /**
     * Creates a typed ingredient from the given ingredient.
     *
     * @param ingredient The ingredient to create a typed ingredient from.
     * @param <T>        The type of the ingredient.
     * @return The created typed ingredient, or null if it couldn't be created.
     */
    public static <T> ITypedIngredient<?> createTypedIngredient(T ingredient) {
        return TypedIngredient.create(JeiRecipeHistoryPlugin.registeredIngredients, ingredient).orElse(null);
    }
}
