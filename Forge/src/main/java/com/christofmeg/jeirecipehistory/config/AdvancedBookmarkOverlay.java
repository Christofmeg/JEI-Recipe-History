package com.christofmeg.jeirecipehistory.config;

import com.christofmeg.jeirecipehistory.mixin.accessor.BookmarkOverlayAccessor;
import com.christofmeg.jeirecipehistory.recipe.IRecipeInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.bookmarks.BookmarkList;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.core.config.IClientConfig;
import mezz.jei.core.config.IWorldConfig;
import mezz.jei.gui.GuiScreenHelper;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.bookmarks.BookmarkOverlay;
import mezz.jei.gui.textures.Textures;
import mezz.jei.input.mouse.IUserInputHandler;
import mezz.jei.input.mouse.handlers.CombinedInputHandler;
import mezz.jei.input.mouse.handlers.ProxyInputHandler;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

import static mezz.jei.gui.overlay.IngredientGrid.INGREDIENT_HEIGHT;
import static mezz.jei.gui.overlay.IngredientGrid.INGREDIENT_WIDTH;

/**
 * The AdvancedBookmarkOverlay class represents an overlay that displays bookmarks and additional functionality.
 * It extends the BookmarkOverlay class.
 */
public class AdvancedBookmarkOverlay extends BookmarkOverlay {

    private static final int SPACE_BETWEEN_BUTTONS = 2;

    private static final int BUTTON_SIZE = 20;
    private static final int BUTTON_SIZE_BOOKMARK = 20;
    private static final int BUTTON_SIZE_RECIPE_HISTORY_CONFIG = 20;
    private final BookmarkOverlayAccessor accessor = (BookmarkOverlayAccessor) this;
    private final GuiIconToggleButton recordConfigButton;
    private final IngredientGridWithNavigation contents;
    
    private @Nullable RecipeLayoutLite recipeLayout;
    
    private IRecipeInfo infoUnderMouse;

    /**
     * Creates an instance of AdvancedBookmarkOverlay or BookmarkOverlay based on the configuration.
     *
     * @param bookmarkList The BookmarkList object.
     * @param textures The Textures object.
     * @param contents The IngredientGridWithNavigation object representing the contents of the overlay.
     * @param clientConfig The IClientConfig object representing the client configuration.
     * @param worldConfig The IWorldConfig object representing the world configuration.
     * @param guiScreenHelper The GuiScreenHelper object.
     * @param serverConnection The IConnectionToServer object representing the server connection.
     * @return An instance of AdvancedBookmarkOverlay or BookmarkOverlay based on the configuration.
     */
    // Used in constructor_redirect.js
    @SuppressWarnings("unused")
    public static BookmarkOverlay create(
            BookmarkList bookmarkList,
            Textures textures,
            IngredientGridWithNavigation contents,
            IClientConfig clientConfig,
            IWorldConfig worldConfig,
            GuiScreenHelper guiScreenHelper,
            IConnectionToServer serverConnection
    ) {
        if(!JeiRecipeHistoryConfig.isAllModFeatuesDisabled()){
            return new AdvancedBookmarkOverlay(bookmarkList, textures, contents, clientConfig, worldConfig, guiScreenHelper, serverConnection);
        }
        else {
            return new BookmarkOverlay(bookmarkList, textures, contents, clientConfig, worldConfig, guiScreenHelper, serverConnection);
        }
    }

    /**
     * Creates an instance of AdvancedBookmarkOverlay.
     *
     * @param bookmarkList The BookmarkList object.
     * @param textures The Textures object.
     * @param contents The IngredientGridWithNavigation object representing the contents of the overlay.
     * @param clientConfig The IClientConfig object representing the client configuration.
     * @param worldConfig The IWorldConfig object representing the world configuration.
     * @param guiScreenHelper The GuiScreenHelper object.
     * @param serverConnection The IConnectionToServer object representing the server connection.
     */
    public AdvancedBookmarkOverlay(BookmarkList bookmarkList, Textures textures, IngredientGridWithNavigation contents, IClientConfig clientConfig, IWorldConfig worldConfig, GuiScreenHelper guiScreenHelper, IConnectionToServer serverConnection) {
        super(bookmarkList, textures, contents, clientConfig, worldConfig, guiScreenHelper, serverConnection);
        this.contents = accessor.getContents();
        this.recordConfigButton = ModConfigButton.create(this);
    }

    /**
     * Updates the bounds of the overlay based on the provided exclusion areas.
     *
     * @param guiExclusionAreas A set of ImmutableRect2i objects representing the exclusion areas on the GUI.
     * @return true if the contents of the overlay have enough room after the update, false otherwise.
     */
    @Override
    public boolean updateBounds(@NotNull Set<ImmutableRect2i> guiExclusionAreas) {
        ImmutableRect2i parentArea = accessor.getParentArea();
        ImmutableRect2i availableContentsArea = parentArea.cropBottom(BUTTON_SIZE + SPACE_BETWEEN_BUTTONS);
        boolean contentsHasRoom = contents.updateBounds(availableContentsArea, guiExclusionAreas);
        ImmutableRect2i contentsArea = contents.getBackgroundArea();
        ImmutableRect2i bookmarkButtonArea = parentArea
                .matchWidthAndX(contentsArea)
                .keepBottom(BUTTON_SIZE_BOOKMARK)
                .keepLeft(BUTTON_SIZE_BOOKMARK);
/*
     TODO if JEIUtilities releases updated version
  if(ModList.get().isLoaded(Constants.JEI_UTILITIES)) {
      ImmutableRect2i recordConfigArea = parentArea
              .matchWidthAndX(contentsArea)
              .keepBottom(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
              .keepLeft(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
              .addOffset(BUTTON_SIZE + BUTTON_SIZE_RECIPE_HISTORY_CONFIG + SPACE_BETWEEN_BUTTONS, 0);
      this.recordConfigButton.updateBounds(recordConfigArea);


  }
  else {


      ImmutableRect2i recordConfigArea = parentArea
              .matchWidthAndX(contentsArea)
              .keepBottom(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
              .keepLeft(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
              .addOffset(BUTTON_SIZE_RECIPE_HISTORY_CONFIG + SPACE_BETWEEN_BUTTONS, 0);
      this.recordConfigButton.updateBounds(recordConfigArea);
  }
*/

        if(!JeiRecipeHistoryConfig.isAllModFeatuesDisabled()){
            ImmutableRect2i recordConfigArea = parentArea
                    .matchWidthAndX(contentsArea)
                    .keepBottom(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
                    .keepLeft(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
                    .addOffset(BUTTON_SIZE_RECIPE_HISTORY_CONFIG + SPACE_BETWEEN_BUTTONS, 0);
            this.recordConfigButton.updateBounds(recordConfigArea);
        }
        accessor.getBookmarkButton().updateBounds(bookmarkButtonArea);
        if (contentsHasRoom) {
            contents.updateLayout(false);
        }
        return contentsHasRoom;
    }

    /**
     * Draws the overlay on the screen.
     *
     * @param minecraft The Minecraft instance.
     * @param poseStack The PoseStack instance.
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param partialTicks The partial tick time.
     */
    @Override
    public void drawScreen(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(minecraft, poseStack, mouseX, mouseY, partialTicks);
        if(!JeiRecipeHistoryConfig.isAllModFeatuesDisabled()) {
            this.recordConfigButton.draw(poseStack, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * Draws tooltips on the overlay.
     *
     * @param minecraft The Minecraft instance.
     * @param poseStack The PoseStack instance.
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     */
    @Override
    public void drawTooltips(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
        if(!JeiRecipeHistoryConfig.isAllModFeatuesDisabled()) {
            boolean renderRecipe = false;
            Optional<ITypedIngredient<?>> ingredient = getIngredientUnderMouse();
            if (ingredient.isPresent() && ingredient.get().getIngredient() instanceof IRecipeInfo info) {
                @Nullable RecipeLayoutLite recipeLayout;
                if (this.infoUnderMouse == info) {
                    recipeLayout = this.recipeLayout;
                } else {
                    this.infoUnderMouse = info;
                    recipeLayout = RecipeLayoutLite.create(info.getRecipeCategory(), info.getRecipe(), info.getFocusGroup(), mouseX, mouseY);
                    this.recipeLayout = recipeLayout;
                }
                if (recipeLayout != null) {
                    updatePosition(mouseX, mouseY);
                    recipeLayout.drawRecipe(poseStack, mouseX, mouseY);
                    renderRecipe = true;
                }
            }
            if (!renderRecipe) {
                super.drawTooltips(minecraft, poseStack, mouseX, mouseY);
                this.recordConfigButton.drawTooltips(poseStack, mouseX, mouseY);
            }
        }
    }

    private void updatePosition(int mouseX, int mouseY) {
        if (this.recipeLayout != null) {
            int x = this.recipeLayout.getPosX();
            int y = this.recipeLayout.getPosY();
            ImmutableRect2i area = new ImmutableRect2i(x - INGREDIENT_WIDTH, y - INGREDIENT_WIDTH, INGREDIENT_WIDTH * 2, INGREDIENT_HEIGHT * 2);
            if (!area.contains(mouseX, mouseY)) {
                this.recipeLayout.setPosition(mouseX, mouseY);
            }
        }
    }

    /**
     * Creates and returns the input handler for the overlay.
     *
     * @return The IUserInputHandler object representing the input handler.
     */
    @Override
    public @NotNull IUserInputHandler createInputHandler() {
        final IUserInputHandler bookmarkButtonInputHandler = accessor.getBookmarkButton().createInputHandler();
        final IUserInputHandler recordConfigButtonInputHandler = this.recordConfigButton.createInputHandler();
        final IUserInputHandler displayedInputHandler = new CombinedInputHandler(
                accessor.getCheatInputHandler(),
                accessor.getContents().createInputHandler(),
                bookmarkButtonInputHandler,
                recordConfigButtonInputHandler
        );
        return new ProxyInputHandler(() -> {
            if (isListDisplayed()) {
                return displayedInputHandler;
            }
            return new CombinedInputHandler(bookmarkButtonInputHandler, recordConfigButtonInputHandler);
        });
    }

    /**
     * Returns the RecipeLayoutLite object associated with the overlay.
     *
     * @return The RecipeLayoutLite object representing the recipe layout.
     */
    public @Nullable RecipeLayoutLite getRecipeLayout() {
        return recipeLayout;
    }

    /**
     * Returns the IRecipeInfo object under the mouse.
     *
     * @return The IRecipeInfo object under the mouse.
     */
    public IRecipeInfo getInfoUnderMouse() {
        return infoUnderMouse;
    }

    /**
     * Sets the IRecipeInfo object under the mouse.
     *
     * @param infoUnderMouse The IRecipeInfo object to set as the info under the mouse.
     */
    public void setInfoUnderMouse(IRecipeInfo<String, Integer> infoUnderMouse) {
        this.infoUnderMouse = infoUnderMouse;
    }

    /**
     * Sets the RecipeLayoutLite object associated with the overlay.
     *
     * @param recipeLayout The RecipeLayoutLite object to set as the recipe layout.
     */
    public void setRecipeLayout(@Nullable RecipeLayoutLite recipeLayout) {
        this.recipeLayout = recipeLayout;
    }

}
