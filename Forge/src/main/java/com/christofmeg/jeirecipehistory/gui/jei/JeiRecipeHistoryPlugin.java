package com.christofmeg.jeirecipehistory.gui.jei;

import com.christofmeg.jeirecipehistory.Constants;
import com.christofmeg.jeirecipehistory.mixin.accessor.RecipeGuiLogicAccessor;
import com.christofmeg.jeirecipehistory.mixin.accessor.RecipesGuiAccessor;
import com.christofmeg.jeirecipehistory.gui.history.AdvancedIngredientListGrid;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IBookmarkOverlay;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.gui.recipes.IRecipeGuiLogic;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.ingredients.RegisteredIngredients;
import mezz.jei.recipes.RecipeTransferManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JeiRecipeHistoryPlugin implements IModPlugin {

    public static IRecipeManager recipeManager;
    public static IIngredientManager ingredientManager;
    public static IJeiHelpers jeiHelpers;
    public static IGuiHelper guiHelper;
    public static IModIdHelper modIdHelper;
    public static IFocusFactory focusFactory;
    public static IRecipeGuiLogic logic;
    public static RegisteredIngredients registeredIngredients;
    public static IIngredientListOverlay ingredientListOverlay;
    public static RecipesGui recipesGui;
    public static RecipeTransferManager recipeTransferManager;
    public static AdvancedIngredientListGrid historyGrid;
    public static IBookmarkOverlay bookmarkOverlay;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(Constants.MOD_ID, "jei");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        ingredientListOverlay = jeiRuntime.getIngredientListOverlay();
        recipesGui = (RecipesGui) jeiRuntime.getRecipesGui();
        logic = ((RecipesGuiAccessor) recipesGui).getLogic();
        registeredIngredients = ((RecipeGuiLogicAccessor) logic).getRegisteredIngredients();
        recipeTransferManager = ((RecipesGuiAccessor) recipesGui).getRecipeTransferManager();
        bookmarkOverlay = jeiRuntime.getBookmarkOverlay();
        guiHelper = jeiRuntime.getJeiHelpers().getGuiHelper();
    }

    /**
     * Called by ASM in constructor_redirect3.js
     */
    @SuppressWarnings("unused")
    public static void setEarlyValue(IRecipeManager recipeManager, IJeiHelpers jeiHelpers, IIngredientManager ingredientManager) {
        JeiRecipeHistoryPlugin.recipeManager = recipeManager;
        JeiRecipeHistoryPlugin.jeiHelpers = jeiHelpers;
        JeiRecipeHistoryPlugin.focusFactory = jeiHelpers.getFocusFactory();
        JeiRecipeHistoryPlugin.ingredientManager = ingredientManager;
        JeiRecipeHistoryPlugin.guiHelper = jeiHelpers.getGuiHelper();
        JeiRecipeHistoryPlugin.modIdHelper = jeiHelpers.getModIdHelper();
    }




}
