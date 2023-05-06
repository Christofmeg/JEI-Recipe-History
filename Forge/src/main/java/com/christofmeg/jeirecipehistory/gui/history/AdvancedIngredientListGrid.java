package com.christofmeg.jeirecipehistory.gui.history;

import com.christofmeg.jeirecipehistory.config.JeiRecipeHistoryConfig;
import com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin;
import com.christofmeg.jeirecipehistory.mixin.accessor.IngredientGridAccessor;
import com.christofmeg.jeirecipehistory.recipe.IRecipeInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.config.IEditModeConfig;
import mezz.jei.config.IIngredientFilterConfig;
import mezz.jei.config.IIngredientGridConfig;
import mezz.jei.core.config.IClientConfig;
import mezz.jei.core.config.IWorldConfig;
import mezz.jei.gui.GuiScreenHelper;
import mezz.jei.gui.overlay.IngredientGrid;
import mezz.jei.ingredients.RegisteredIngredients;
import mezz.jei.ingredients.TypedIngredient;
import mezz.jei.input.IClickedIngredient;
import mezz.jei.input.mouse.handlers.DeleteItemInputHandler;
import mezz.jei.render.ElementRenderer;
import mezz.jei.render.IngredientListRenderer;
import mezz.jei.render.IngredientListSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin.registeredIngredients;
import static com.christofmeg.jeirecipehistory.helper.IngredientHelper.createTypedIngredient;

/**
 * Replace ingredientListGrid
 */
@SuppressWarnings("unused")
public class AdvancedIngredientListGrid extends IngredientGrid {

    private static final int INGREDIENT_PADDING = 1;
    private static final int MIN_ROWS = 5;
    private final IngredientGridAccessor accessor = (IngredientGridAccessor) this;
    private final IngredientListRenderer historyListRender;
    private final List<ITypedIngredient<?>> historyList;
    private boolean showHistory;
    private int historyMaxSize;
    private int historyHeight;

<<<<<<< Updated upstream
    public static IngredientGrid create(
            RegisteredIngredients registeredIngredients,
            IIngredientGridConfig gridConfig,
            IEditModeConfig editModeConfig,
            IIngredientFilterConfig ingredientFilterConfig,
            IClientConfig clientConfig,
            IWorldConfig worldConfig,
            GuiScreenHelper guiScreenHelper,
            IModIdHelper modIdHelper,
            IConnectionToServer serverConnection
    ) {
        return JeiRecipeHistoryPlugin.historyGrid = new AdvancedIngredientListGrid(
                registeredIngredients,
                gridConfig,
                editModeConfig,
                ingredientFilterConfig,
                clientConfig,
                worldConfig,
                guiScreenHelper,
                modIdHelper,
                serverConnection);
=======
    @SuppressWarnings("unused")
    public static IngredientGrid create(RegisteredIngredients registeredIngredients, IIngredientGridConfig gridConfig, IEditModeConfig editModeConfig, IIngredientFilterConfig ingredientFilterConfig, IClientConfig clientConfig, IWorldConfig worldConfig, GuiScreenHelper guiScreenHelper, IModIdHelper modIdHelper, IConnectionToServer serverConnection
    ) {
        if(JeiRecipeHistoryConfig.isAllModFeatuesDisabled()) {
            return new IngredientGrid(registeredIngredients, gridConfig, editModeConfig, ingredientFilterConfig, clientConfig, worldConfig, guiScreenHelper, modIdHelper, serverConnection);
        } else {
            return JeiRecipeHistoryPlugin.historyGrid = new AdvancedIngredientListGrid(registeredIngredients, gridConfig, editModeConfig, ingredientFilterConfig, clientConfig, worldConfig, guiScreenHelper, modIdHelper, serverConnection);
        }
>>>>>>> Stashed changes
    }

    public AdvancedIngredientListGrid(
            RegisteredIngredients registeredIngredients,
            IIngredientGridConfig gridConfig,
            IEditModeConfig editModeConfig,
            IIngredientFilterConfig ingredientFilterConfig,
            IClientConfig clientConfig,
            IWorldConfig worldConfig,
            GuiScreenHelper guiScreenHelper,
            IModIdHelper modIdHelper,
            IConnectionToServer serverConnection) {
        super(registeredIngredients,
                gridConfig,
                editModeConfig,
                ingredientFilterConfig,
                clientConfig,
                worldConfig,
                guiScreenHelper,
                modIdHelper,
                serverConnection
        );
        this.historyListRender = new IngredientListRenderer(editModeConfig, worldConfig, registeredIngredients);
        this.historyList = new ArrayList<>();
    }

    /**
     * Draws Ingredient list, config buttons, bookmarks and recipe history on screen
     */
    @Override
    public boolean updateBounds(@NotNull ImmutableRect2i availableArea, @NotNull Collection<ImmutableRect2i> exclusionAreas) {
        /**
         * Updates ingredient list
         */
        accessor.getIngredientListRenderer().clear();
        this.historyListRender.clear();

        /**
         * Draws searchbar and ingredient grid pages
         */
        accessor.setArea(calculateBounds(accessor.getGridConfig(), availableArea));
        ImmutableRect2i area = this.getArea();
        if (area.isEmpty()) {
            return false;
        }
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
        historyHeight = showHistory ? JeiRecipeHistoryConfig.getRowCount() * INGREDIENT_HEIGHT : 0;

        for (int y = area.getY(); y < area.getY() + area.getHeight() - historyHeight; y += INGREDIENT_HEIGHT) {
            for (int x = area.getX(); x < area.getX() + area.getWidth(); x += INGREDIENT_WIDTH) {
                IngredientListSlot ingredientListSlot = new IngredientListSlot(x, y, INGREDIENT_WIDTH, INGREDIENT_HEIGHT, INGREDIENT_PADDING);
                ImmutableRect2i stackArea = ingredientListSlot.getArea();
                final boolean blocked = MathUtil.intersects(exclusionAreas, stackArea);
                ingredientListSlot.setBlocked(blocked);
                accessor.getIngredientListRenderer().add(ingredientListSlot);
            }
        }

        if (showHistory) {
            int startY = area.getY() + area.getHeight() - historyHeight;
            for (int y = startY; y < area.getY() + area.getHeight(); y += INGREDIENT_HEIGHT) {
                for (int x = area.getX(); x < area.getX() + area.getWidth(); x += INGREDIENT_WIDTH) {
                    IngredientListSlot ingredientListSlot = new IngredientListSlot(x, y, INGREDIENT_WIDTH, INGREDIENT_HEIGHT, INGREDIENT_PADDING);
                    ImmutableRect2i stackArea = ingredientListSlot.getArea();
                    final boolean blocked = MathUtil.intersects(exclusionAreas, stackArea);
                    ingredientListSlot.setBlocked(blocked);
                    historyListRender.add(ingredientListSlot);
                }
            }
            this.historyListRender.set(0, this.historyList);
        }
<<<<<<< Updated upstream
=======
        /**
         * Makes sure the recipe history renders items when gui screen size changes
         */
        this.historyListRender.set(0, this.historyList);

        if (!JeiRecipeHistoryConfig.isRecipeHistoryEnabled() || JeiRecipeHistoryConfig.isAllModFeatuesDisabled()) {
            accessor.getIngredientListRenderer().clear();
            this.historyListRender.clear();

            accessor.setArea(calculateBounds(accessor.getGridConfig(), availableArea));
            if (area.isEmpty()) {
                return false;
            }

            for (int y = area.getY(); y < area.getY() + area.getHeight(); y += INGREDIENT_HEIGHT) {
                for (int x = area.getX(); x < area.getX() + area.getWidth(); x += INGREDIENT_WIDTH) {
                    IngredientListSlot ingredientListSlot = new IngredientListSlot(x, y, INGREDIENT_WIDTH, INGREDIENT_HEIGHT, INGREDIENT_PADDING);
                    ImmutableRect2i stackArea = ingredientListSlot.getArea();
                    final boolean blocked = MathUtil.intersects(exclusionAreas, stackArea);
                    ingredientListSlot.setBlocked(blocked);
                    accessor.getIngredientListRenderer().add(ingredientListSlot);
                }
            }
        }
>>>>>>> Stashed changes

        return true;
    }

    private ImmutableRect2i calculateBounds(@NotNull IIngredientGridConfig config, @NotNull ImmutableRect2i availableArea) {
        final int columns = Math.min(availableArea.getWidth() / IngredientGrid.INGREDIENT_WIDTH, config.getMaxColumns());
        final int rows = Math.min(availableArea.getHeight() / IngredientGrid.INGREDIENT_HEIGHT, config.getMaxRows());
        this.showHistory = rows - JeiRecipeHistoryConfig.getRowCount() >= MIN_ROWS;

        if (rows < config.getMinRows() || columns < config.getMinColumns()) {
            return ImmutableRect2i.EMPTY;
        }
        this.historyMaxSize = JeiRecipeHistoryConfig.getRowCount() * columns;
        final int width = columns * IngredientGrid.INGREDIENT_WIDTH;
        final int height = rows * IngredientGrid.INGREDIENT_HEIGHT;

        final int x = switch (config.getHorizontalAlignment()) {
            case LEFT -> availableArea.getX();
            case CENTER -> availableArea.getX() + ((availableArea.getWidth() - width) / 2);
            case RIGHT -> availableArea.getX() + (availableArea.getWidth() - width);
        };

        final int y = switch (config.getVerticalAlignment()) {
            case TOP -> availableArea.getY();
            case CENTER -> availableArea.getY() + ((availableArea.getHeight() - height) / 2);
            case BOTTOM -> availableArea.getY() + (availableArea.getHeight() - height);
        };

        return new ImmutableRect2i(x, y, width, height);
    }

    @Override
    public void draw(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
        super.draw(minecraft, poseStack, mouseX, mouseY);
        if (showHistory) {
            RenderSystem.disableBlend();
            this.historyListRender.render(poseStack);
            if (isMouseOver(mouseX, mouseY)) {
                DeleteItemInputHandler deleteItemHandler = (DeleteItemInputHandler) this.getInputHandler();
                if (!deleteItemHandler.shouldDeleteItemOnClick(minecraft, mouseX, mouseY)) {
                    this.historyListRender.getSlots()
                            .filter(s -> s.isMouseOver(mouseX, mouseY))
                            .map(IngredientListSlot::getIngredientRenderer)
                            .flatMap(Optional::stream)
                            .map(ElementRenderer::getArea)
                            .findFirst()
                            .ifPresent(area -> drawHighlight(poseStack, area));
                }
<<<<<<< Updated upstream
=======
                ImmutableRect2i area = this.getArea();
                int endX = area.getX() + area.getWidth();
                int startY = area.getY() + area.getHeight() - historyHeight;
                int endY = area.getY() + area.getHeight();
                int colour = JeiRecipeHistoryConfig.getBorderTint();

                drawHorizontalDashedLine(poseStack, area.getX(), endX, startY, colour, false);
                drawHorizontalDashedLine(poseStack, area.getX(), endX, endY, colour, true);

                drawVerticalDashedLine(poseStack, area.getX(), startY, endY, colour, false);
                drawVerticalDashedLine(poseStack, endX - 1, startY, endY, colour, true);
>>>>>>> Stashed changes
            }
            ImmutableRect2i area = this.getArea();
            int endX = area.getX() + area.getWidth();
            int startY = area.getY() + area.getHeight() - historyHeight;
            int endY = area.getY() + area.getHeight();
            int colour = JeiRecipeHistoryConfig.getBorderTint();

            drawHorizontalDashedLine(poseStack, area.getX(), endX, startY, colour, false);
            drawHorizontalDashedLine(poseStack, area.getX(), endX, endY, colour, true);

            drawVerticalDashedLine(poseStack, area.getX(), startY, endY, colour, false);
            drawVerticalDashedLine(poseStack, endX - 1, startY, endY, colour, true);


        }
    }

    @Override
    public void drawTooltips(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
        super.drawTooltips(minecraft, poseStack, mouseX, mouseY);
        if (isMouseOver(mouseX, mouseY)) {
            this.historyListRender.getSlots()
                    .filter(s -> s.isMouseOver(mouseX, mouseY))
                    .map(IngredientListSlot::getTypedIngredient)
                    .flatMap(Optional::stream)
                    .findFirst()
                    .ifPresent(ingredient -> accessor.getTooltipHelper().drawTooltip(poseStack, mouseX, mouseY, ingredient));
        }
    }

    @Override
    public @NotNull Stream<IClickedIngredient<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
        return Stream.concat(
                super.getIngredientUnderMouse(mouseX, mouseY),
                historyListRender.getSlots()
                        .filter(s -> s.isMouseOver(mouseX, mouseY))
                        .map(IngredientListSlot::getIngredientRenderer)
                        .flatMap(Optional::stream)
        );
    }

    /**
     * Copied from <a href="https://github.com/shedaniel/RoughlyEnoughItems/blob/8.x-1.18.2/runtime/src/main/java/me/shedaniel/rei/impl/client/gui/widget/favorites/history/DisplayHistoryWidget.java">...</a>
     */
    private void drawHorizontalDashedLine(PoseStack poseStack, int x1, int x2, int y, int color, boolean reverse) {
        float offset = (System.currentTimeMillis() % 600) / 100.0F;
        if (!reverse) offset = 6 - offset;

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        Matrix4f pose = poseStack.last().pose();

        for (float x = x1 - offset; x < x2; x += 7) {
            builder.vertex(pose, Mth.clamp(x + 4, x1, x2), y, 0).color(r, g, b, a).endVertex();
            builder.vertex(pose, Mth.clamp(x, x1, x2), y, 0).color(r, g, b, a).endVertex();
            builder.vertex(pose, Mth.clamp(x, x1, x2), y + 1, 0).color(r, g, b, a).endVertex();
            builder.vertex(pose, Mth.clamp(x + 4, x1, x2), y + 1, 0).color(r, g, b, a).endVertex();
        }

        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    /**
     * Copied from <a href="https://github.com/shedaniel/RoughlyEnoughItems/blob/8.x-1.18.2/runtime/src/main/java/me/shedaniel/rei/impl/client/gui/widget/favorites/history/DisplayHistoryWidget.java">...</a>
     */
    private void drawVerticalDashedLine(PoseStack poseStack, int x, int y1, int y2, int color, boolean reverse) {
        float offset = (System.currentTimeMillis() % 600) / 100.0F;
        if (!reverse) offset = 6 - offset;

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        Matrix4f pose = poseStack.last().pose();

        for (float y = y1 - offset; y < y2; y += 7) {
            builder.vertex(pose, x + 1, Mth.clamp(y, y1, y2), 0).color(r, g, b, a).endVertex();
            builder.vertex(pose, x, Mth.clamp(y, y1, y2), 0).color(r, g, b, a).endVertex();
            builder.vertex(pose, x, Mth.clamp(y + 4, y1, y2), 0).color(r, g, b, a).endVertex();
            builder.vertex(pose, x + 1, Mth.clamp(y + 4, y1, y2), 0).color(r, g, b, a).endVertex();
        }

        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public void addHistory(@NotNull ITypedIngredient<?> ingredient) {
        if (ingredient.getIngredient() instanceof IRecipeInfo recipeInfo) {
            ingredient = createTypedIngredient(recipeInfo.getOutput());
        }
        if (ingredient != null) {
            Optional<? extends ITypedIngredient<?>> normalized = TypedIngredient.normalize(registeredIngredients, ingredient);
            if (normalized.isPresent()) {
                ingredient = normalized.get();
                IIngredientHelper ingredientHelper = registeredIngredients.getIngredientHelper(ingredient.getType());
                String uniqueId = ingredientHelper.getUniqueId(ingredient.getIngredient(), UidContext.Ingredient);
                @NotNull ITypedIngredient<?> value = ingredient;
                historyList.removeIf(element -> equal(ingredientHelper, value, uniqueId, element));
                historyList.add(0, ingredient);
                if (historyList.size() > historyMaxSize) {
                    historyList.remove(historyMaxSize);
                }
                historyListRender.set(0, historyList);
            }
        }
    }

    /**
     * copy from mezz.jei.bookmarks.BookmarkList#equal(IIngredientHelper, ITypedIngredient, String, ITypedIngredient)
     */
    private static <T> boolean equal(IIngredientHelper<T> ingredientHelper, @NotNull ITypedIngredient<T> a, String uidA, @NotNull ITypedIngredient<?> b) {
        if (a.getIngredient() == b.getIngredient()) {
            return true;
        }

        if (a.getIngredient() instanceof ItemStack itemStackA && b.getIngredient() instanceof ItemStack itemStackB) {
            if (JeiRecipeHistoryConfig.shouldMatchNBTTags()) {
                return itemStackA.equals(itemStackB, true);
            } else {
                return ItemStack.isSame(itemStackA, itemStackB);
            }
        }

        Optional<T> filteredB = b.getIngredient(a.getType());
        if (filteredB.isPresent()) {
            T ingredientB = filteredB.get();
            String uidB = ingredientHelper.getUniqueId(ingredientB, UidContext.Ingredient);
            return uidA.equals(uidB);
        }

        return false;
    }

}
