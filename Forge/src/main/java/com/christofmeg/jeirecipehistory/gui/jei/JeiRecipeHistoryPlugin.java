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
import mezz.jei.api.runtime.*;
/*
import mezz.jei.gui.recipes.IRecipeGuiLogic;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.ingredients.RegisteredIngredients;
import mezz.jei.recipes.RecipeTransferManager;
 */
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Plugin class for JEI Recipe History. Implements the {@link IModPlugin} interface.
 */
@JeiPlugin
public class JeiRecipeHistoryPlugin implements IModPlugin {

    /** Reference to the recipe manager. */
    public static IRecipeManager recipeManager;
    /** Reference to the ingredient manager. */
    public static IIngredientManager ingredientManager;
    /** Reference to the JEI helpers. */
    public static IJeiHelpers jeiHelpers;
    /** Reference to the GUI helper. */
    public static IGuiHelper guiHelper;
    /** Reference to the mod ID helper. */
    public static IModIdHelper modIdHelper;
    /** Reference to the focus factory. */
    public static IFocusFactory focusFactory;
    /** Reference to the recipe GUI logic. */
    public static IRecipeGuiLogic logic;
    /** Reference to the registered ingredients. */
    public static RegisteredIngredients registeredIngredients;
    /** Reference to the ingredient list overlay. */
    public static IIngredientListOverlay ingredientListOverlay;
    /** Reference to the recipes GUI. */
    public static IRecipesGui recipesGui;
    /** Reference to the recipe transfer manager. */
    public static RecipeTransferManager recipeTransferManager;
    /** Reference to the advanced ingredient list grid. */
    public static AdvancedIngredientListGrid historyGrid;
    /** Reference to the bookmark overlay. */
    public static IBookmarkOverlay bookmarkOverlay;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(Constants.MOD_ID, "jei");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        ingredientListOverlay = jeiRuntime.getIngredientListOverlay();
        recipesGui = jeiRuntime.getRecipesGui();
        logic = ((RecipesGuiAccessor) recipesGui).getLogic();
        registeredIngredients = ((RecipeGuiLogicAccessor) logic).getRegisteredIngredients();
        recipeTransferManager = ((RecipesGuiAccessor) recipesGui).getRecipeTransferManager();
        bookmarkOverlay = jeiRuntime.getBookmarkOverlay();
        guiHelper = jeiRuntime.getJeiHelpers().getGuiHelper();
    }

    /**
     * Called by ASM in constructor_redirect3.js
     *
     * @param recipeManager    The recipe manager instance.
     * @param jeiHelpers       The JEI helpers instance.
     * @param ingredientManager The ingredient manager instance.
     */
     // Called by ASM in constructor_redirect3.js
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
