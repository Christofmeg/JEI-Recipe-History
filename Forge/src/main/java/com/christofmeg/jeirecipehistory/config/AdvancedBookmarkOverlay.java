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

public class AdvancedBookmarkOverlay extends BookmarkOverlay {

    private static final int SPACE_BETWEEN_BUTTONS = 2;


    private static final int BUTTON_SIZE = 20;
    private static final int BUTTON_SIZE_BOOKMARK = 20;
    private static final int BUTTON_SIZE_RECIPE_HISTORY_CONFIG = 20;
    private final BookmarkOverlayAccessor accessor = (BookmarkOverlayAccessor) this;
    private final GuiIconToggleButton recordConfigButton;
    private final IngredientGridWithNavigation contents;
    
    @Nullable
    private RecipeLayoutLite<Object> recipeLayout;
    
    private IRecipeInfo<String, Integer> infoUnderMouse;

    /**
     * Used in constructor_redirect.js
     */
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
            return new AdvancedBookmarkOverlay(bookmarkList, textures, contents, clientConfig, worldConfig, guiScreenHelper, serverConnection);
    }

    public AdvancedBookmarkOverlay(BookmarkList bookmarkList, Textures textures, IngredientGridWithNavigation contents, IClientConfig clientConfig, IWorldConfig worldConfig, GuiScreenHelper guiScreenHelper, IConnectionToServer serverConnection) {
        super(bookmarkList, textures, contents, clientConfig, worldConfig, guiScreenHelper, serverConnection);
        this.contents = accessor.getContents();
        this.recordConfigButton = ModConfigButton.create(this);
    }

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
        ImmutableRect2i recordConfigArea = parentArea
                .matchWidthAndX(contentsArea)
                .keepBottom(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
                .keepLeft(BUTTON_SIZE_RECIPE_HISTORY_CONFIG)
                .addOffset(BUTTON_SIZE_RECIPE_HISTORY_CONFIG + SPACE_BETWEEN_BUTTONS, 0);
        this.recordConfigButton.updateBounds(recordConfigArea);

        accessor.getBookmarkButton().updateBounds(bookmarkButtonArea);
        if (contentsHasRoom) {
            contents.updateLayout(false);
        }
        return contentsHasRoom;
    }

    @Override
    public void drawScreen(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(minecraft, poseStack, mouseX, mouseY, partialTicks);
        this.recordConfigButton.draw(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawTooltips(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
        boolean renderRecipe = false;
        Optional<ITypedIngredient<?>> ingredient = getIngredientUnderMouse();
        if (ingredient.isPresent() && ingredient.get().getIngredient() instanceof IRecipeInfo info) {
            RecipeLayoutLite<Object> recipeLayout;
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
    
    public @Nullable RecipeLayoutLite<Object> getRecipeLayout() {
        return recipeLayout;
    }
    
    public IRecipeInfo<String, Integer> getInfoUnderMouse() {
        return infoUnderMouse;
    }
    
    public void setInfoUnderMouse(IRecipeInfo<String, Integer> infoUnderMouse) {
        this.infoUnderMouse = infoUnderMouse;
    }

    public void setRecipeLayout(@Nullable RecipeLayoutLite recipeLayout) {
        this.recipeLayout = recipeLayout;
    }

}
