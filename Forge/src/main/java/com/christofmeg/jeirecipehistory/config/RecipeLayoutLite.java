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
import mezz.jei.gui.ingredients.RecipeSlot;
import mezz.jei.gui.ingredients.RecipeSlots;
import mezz.jei.gui.recipes.OutputSlotTooltipCallback;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.builder.RecipeLayoutBuilder;
import mezz.jei.ingredients.RegisteredIngredients;
import mezz.jei.transfer.RecipeTransferUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeLayoutLite<R> extends RecipeLayout<R> {
    private final RegisteredIngredients registeredIngredients;
    private final RecipeLayoutAccessor accessor = (RecipeLayoutAccessor) this;

    @Nullable
    private IRecipeTransferError error;

    public RecipeLayoutLite(
            IRecipeCategory<R> recipeCategory,
            R recipe,
            IFocusGroup focuses,
            RegisteredIngredients registeredIngredients,
            int posX,
            int posY
    ) {
        super(-1, recipeCategory, recipe, focuses, registeredIngredients, posX, posY);
        this.registeredIngredients = registeredIngredients;
        accessor.setRecipeSlots(new RecipeSlots() {
            @Override
            public void draw(@NotNull PoseStack poseStack, int highlightColor, int recipeMouseX, int recipeMouseY) {
                for (RecipeSlot slot : this.getSlots()) {
                    slot.draw(poseStack);
                }
            }
        });
    }

    @Nullable
    public static <T> RecipeLayoutLite<T> create(IRecipeCategory<T> recipeCategory, T recipe, IFocusGroup focuses, int posX, int posY) {
        RecipeLayoutLite<T> recipeLayout = new RecipeLayoutLite<>(recipeCategory, recipe, focuses, JeiRecipeHistoryPlugin.registeredIngredients, posX, posY);
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

    @Nullable
    public static RecipeLayoutLite<?> create(IRecipeInfo recipeInfo, int posX, int posY) {
        return create(recipeInfo.getRecipeCategory(), recipeInfo.getRecipe(), recipeInfo.getFocusGroup(), posX, posY);
    }

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
