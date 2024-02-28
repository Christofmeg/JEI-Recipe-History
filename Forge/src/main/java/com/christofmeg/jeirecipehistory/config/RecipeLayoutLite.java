package com.christofmeg.jeirecipehistory.config;

import com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin;
import com.christofmeg.jeirecipehistory.mixin.accessor.RecipeLayoutAccessor;
import com.christofmeg.jeirecipehistory.recipe.IRecipeInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
/*
import mezz.jei.gui.ingredients.RecipeSlot;
import mezz.jei.gui.ingredients.RecipeSlots;
import mezz.jei.gui.recipes.OutputSlotTooltipCallback;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.builder.RecipeLayoutBuilder;
import mezz.jei.ingredients.RegisteredIngredients;
*/
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Lite version of RecipeLayout that provides a simplified implementation.
 *
 * @param <R> The type of the recipe.
 */
public class RecipeLayoutLite<R> extends RecipeLayout<R> {
    private final RegisteredIngredients registeredIngredients;
    private final RecipeLayoutAccessor accessor = (RecipeLayoutAccessor) this;

    @Nullable
    private static IRecipeTransferError error;

    /**
     * Constructs a new RecipeLayoutLite.
     *
     * @param recipeCategory        The recipe category.
     * @param recipe                The recipe.
     * @param focuses               The focus group.
     * @param registeredIngredients The registered ingredients.
     * @param posX                  The x position.
     * @param posY                  The y position.
     * @param error                 The recipe transfer error (nullable).
     */
    public RecipeLayoutLite(
            IRecipeCategory<R> recipeCategory,
            R recipe,
            IFocusGroup focuses,
            RegisteredIngredients registeredIngredients,
            int posX,
            int posY,
            @Nullable IRecipeTransferError error) {
        super(-1, recipeCategory, recipe, focuses, registeredIngredients, posX, posY);
        this.registeredIngredients = registeredIngredients;
        RecipeLayoutLite.error = error;
        accessor.setRecipeSlots(new RecipeSlots() {
            @Override
            public void draw(@NotNull PoseStack poseStack, int highlightColor, int recipeMouseX, int recipeMouseY) {
                for (RecipeSlot slot : this.getSlots()) {
                    slot.draw(poseStack);
                }
            }
        });
    }

    /**
     * Creates a new RecipeLayoutLite.
     *
     * @param <T>             The type of the recipe.
     * @param recipeCategory The recipe category.
     * @param recipe         The recipe.
     * @param focuses        The focus group.
     * @param posX           The x position.
     * @param posY           The y position.
     * @return The created RecipeLayoutLite, or null if creation failed.
     */
    @Nullable
    public static <T> RecipeLayoutLite<T> create(IRecipeCategory<T> recipeCategory, T recipe, IFocusGroup focuses, int posX, int posY) {
        RecipeLayoutLite<T> recipeLayout = new RecipeLayoutLite<>(recipeCategory, recipe, focuses, JeiRecipeHistoryPlugin.registeredIngredients, posX, posY, error);
        if (

                recipeLayout.setRecipeLayout(recipeCategory, recipe, JeiRecipeHistoryPlugin.registeredIngredients, focuses) ||
                        recipeLayout.getLegacyAdapter().setRecipeLayout(recipeCategory, recipe)
        ) {
            ResourceLocation recipeName = recipeCategory.getRegistryName(recipe);
            if (recipeName != null) {
                addOutputSlotTooltip(recipeLayout, recipeName, JeiRecipeHistoryPlugin.modIdHelper);
            }
            return recipeLayout;
        }
        return null;
    }

    /**
     * Creates a new RecipeLayoutLite from a recipe info.
     *
     * @param recipeInfo The recipe info.
     * @param posX       The x position.
     * @param posY       The y position.
     * @return The created RecipeLayoutLite, or null if creation failed.
     */
    @Nullable
    public static RecipeLayoutLite<?> create(IRecipeInfo recipeInfo, int posX, int posY) {
        return create(recipeInfo.getRecipeCategory(), recipeInfo.getRecipe(), recipeInfo.getFocusGroup(), posX, posY);
    }

    /**
     * Adds an output slot tooltip to the recipe layout.
     *
     * @param recipeLayout  The recipe layout.
     * @param recipeName    The recipe's name.
     * @param modIdHelper   The mod ID helper.
     */
    private static void addOutputSlotTooltip(RecipeLayoutLite<?> recipeLayout, ResourceLocation recipeName, IModIdHelper modIdHelper) {
        RecipeSlots recipeSlots = recipeLayout.getRecipeSlots();
        List<RecipeSlot> outputSlots = recipeSlots.getSlots().stream()
                .filter(r -> r.getRole() == RecipeIngredientRole.OUTPUT)
                .toList();

        if (!outputSlots.isEmpty()) {
            OutputSlotTooltipCallback callback = new OutputSlotTooltipCallback(recipeName, modIdHelper, recipeLayout.registeredIngredients);
            for (RecipeSlot outputSlot : outputSlots) {
                outputSlot.addTooltipCallback(callback);
            }
        }
    }

    /**
     * Sets the recipe layout for the specified recipe category, recipe, registered ingredients, and focus group.
     *
     * @param recipeCategory        The recipe category.
     * @param recipe                The recipe.
     * @param registeredIngredients The registered ingredients.
     * @param focuses               The focus group.
     * @return True if the recipe layout was successfully set, false otherwise.
     */
    private boolean setRecipeLayout(
            IRecipeCategory<R> recipeCategory,
            R recipe,
            RegisteredIngredients registeredIngredients,
            IFocusGroup focuses
    ) {
        RecipeLayoutBuilder builder = new RecipeLayoutBuilder(registeredIngredients, accessor.getOffset());
        try {
            recipeCategory.setRecipe(builder, recipe, focuses);
            if (builder.isUsed()) {
                builder.setRecipeLayout(this, focuses);
                return true;
            }
        } catch (RuntimeException | LinkageError e) {
            accessor.getLogger().error("Error caught from Recipe Category: {}", recipeCategory.getRecipeType().getUid(), e);
        }
        return false;
    }

}
