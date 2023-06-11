package com.christofmeg.jeirecipehistory.gui.input.handler;

import com.christofmeg.jeirecipehistory.config.JeiRecipeHistoryConfig;
import com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin;
import com.christofmeg.jeirecipehistory.recipe.IRecipeInfo;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.config.KeyBindings;
import mezz.jei.gui.Focus;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.input.CombinedRecipeFocusSource;
import mezz.jei.input.IClickedIngredient;
import mezz.jei.input.UserInput;
import mezz.jei.input.mouse.IUserInputHandler;
import mezz.jei.input.mouse.handlers.FocusInputHandler;
import mezz.jei.input.mouse.handlers.LimitedAreaInputHandler;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
* Based on {@link FocusInputHandler}, but with some modifications to facilitate reading recipes from {@link IRecipeInfo}.
*/
@SuppressWarnings("unused")
public class ExtendedFocusInputHandler implements IUserInputHandler {

private static final List<RecipeIngredientRole> SHOW_RECIPE_ROLES = List.of(RecipeIngredientRole.OUTPUT);
private static final List<RecipeIngredientRole> SHOW_USES_ROLES = List.of(RecipeIngredientRole.INPUT, RecipeIngredientRole.CATALYST);
private final CombinedRecipeFocusSource focusSource;
private final RecipesGui recipesGui;

    /**
     * Creates an instance of ExtendedFocusInputHandler.
     *
     * @param focusSource The combined recipe focus source.
     * @param recipesGui  The recipes GUI.
     * @return An instance of ExtendedFocusInputHandler.
     */
    // Adds items to the recipe history list
    @Contract("_, _ -> new")
    @SuppressWarnings("unused")
    public static @NotNull IUserInputHandler create(CombinedRecipeFocusSource focusSource, RecipesGui recipesGui) {
        if(JeiRecipeHistoryConfig.isAllModFeatuesDisabled()) {
            return new FocusInputHandler(focusSource, recipesGui);
        }
        else {
            return new ExtendedFocusInputHandler(focusSource, recipesGui);
        }
    }

    /**
     * Constructs an instance of the ExtendedFocusInputHandler class.
     *
     * @param focusSource The CombinedRecipeFocusSource object used for focus management.
     * @param recipesGui The RecipesGui object associated with the input handler.
     */
    public ExtendedFocusInputHandler(CombinedRecipeFocusSource focusSource, RecipesGui recipesGui) {
        this.focusSource = focusSource;
        this.recipesGui = recipesGui;
    }

    /**
     * Handles user input and returns an optional IUserInputHandler based on the input.
     *
     * @param screen The Screen object associated with the user input.
     * @param input The UserInput object representing the user input.
     * @return An optional IUserInputHandler based on the input.
     */
    @SuppressWarnings("unused")
    public @NotNull Optional<IUserInputHandler> handleUserInput(@NotNull Screen screen, @NotNull UserInput input) {
        return handleOriginalShow(input);
    }

    /**
     * Handles the original "show" functionality based on the user input.
     *
     * @param input The UserInput object representing the user input.
     * @return An optional IUserInputHandler based on the input.
     */
    private Optional<IUserInputHandler> handleOriginalShow(@NotNull UserInput input) {
        return input.is(KeyBindings.showRecipe) ? handleShow(input, SHOW_RECIPE_ROLES)
                : input.is(KeyBindings.showUses) ? handleShow(input, SHOW_USES_ROLES)
                : Optional.empty();
    }

    /**
     * Handles the "show" functionality based on the user input and roles.
     *
     * @param input The UserInput object representing the user input.
     * @param roles The list of RecipeIngredientRole objects representing the roles.
     * @return An optional IUserInputHandler based on the input.
     */
    private Optional<IUserInputHandler> handleShow(@NotNull UserInput input, List<RecipeIngredientRole> roles) {
        boolean simulate = input.isSimulate();
        Optional<IClickedIngredient<?>> optionalClicked = focusSource.getIngredientUnderMouse(input)
                .findFirst();

        optionalClicked.ifPresent(clicked -> {
            if (!simulate) {
                List<IFocus<?>> focuses = roles.stream()
                        .<IFocus<?>>map(role -> new Focus<>(role, clicked.getTypedIngredient()))
                        .toList();

                // Adds items to recipe history only when history is shown
                if (JeiRecipeHistoryConfig.isRecipeHistoryEnabled() && !JeiRecipeHistoryConfig.isAllModFeatuesDisabled()) {
                    JeiRecipeHistoryPlugin.historyGrid.addHistory(clicked.getTypedIngredient());
                }
                recipesGui.show(focuses);
            }
        });

    return optionalClicked.map(clicked -> LimitedAreaInputHandler.create(this, clicked.getArea()));
    }

}