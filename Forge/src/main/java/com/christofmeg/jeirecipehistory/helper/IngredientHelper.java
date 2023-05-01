package com.christofmeg.jeirecipehistory.helper;

import com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.ingredients.TypedIngredient;

public class IngredientHelper {

    public static <T> ITypedIngredient<?> createTypedIngredient(T ingredient) {
        return TypedIngredient.create(JeiRecipeHistoryPlugin.registeredIngredients, ingredient).orElse(null);
    }
}
