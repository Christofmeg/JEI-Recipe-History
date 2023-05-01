package com.christofmeg.jeirecipehistory;

import com.christofmeg.jeirecipehistory.config.AdvancedBookmarkOverlay;
import com.christofmeg.jeirecipehistory.config.RecipeLayoutLite;
import com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin;
import com.christofmeg.jeirecipehistory.recipe.IRecipeInfo;
import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.core.util.ReflectionUtil;
import mezz.jei.input.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.christofmeg.jeirecipehistory.gui.jei.JeiRecipeHistoryPlugin.ingredientListOverlay;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class GuiInputEventHandler {

    private static final ReflectionUtil reflectionUtil = new ReflectionUtil();
    private static final Set<InputConstants.Key> pressedKeys = new HashSet<>();

    @SubscribeEvent
    public static void onKeyboardKeyPressedEvent(ScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        InputConstants.Key input = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
        boolean shouldNotHandleKey = pressedKeys.contains(input) ||
                isContainerTextFieldFocused(event.getScreen()) ||
                (ingredientListOverlay != null && ingredientListOverlay.hasKeyboardFocus());
        if (shouldNotHandleKey) {
            return;
        }

        RecipeLayoutLite<?> recipeLayout = getRecipeLayout();
        if (recipeLayout != null) {
            Minecraft minecraft = event.getScreen().getMinecraft();
            Player player = minecraft.player;
            if (player == null) {
                return;
            }
            if (minecraft.screen instanceof AbstractContainerScreen<?>) {
                event.setCanceled(true);
                pressedKeys.add(input);
            }
        }
    }

    @SubscribeEvent
    public static void onKeyboardKeyReleasedEvent(ScreenEvent.KeyboardKeyReleasedEvent.Pre event) {
        InputConstants.Key input = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
        pressedKeys.remove(input);
    }

    private static RecipeLayoutLite<?> getRecipeLayout() {
        if (JeiRecipeHistoryPlugin.bookmarkOverlay instanceof AdvancedBookmarkOverlay bookmarkOverlay) {
            Optional<ITypedIngredient<?>> ingredient = bookmarkOverlay.getIngredientUnderMouse();
            if (ingredient.isPresent()) {
                if (ingredient.get().getIngredient() instanceof IRecipeInfo recipeInfo) {
                    if (recipeInfo == bookmarkOverlay.getInfoUnderMouse()) {
                        return bookmarkOverlay.getRecipeLayout();
                    } else {
                        RecipeLayoutLite<?> recipeLayout = RecipeLayoutLite.create(recipeInfo, (int) MouseUtil.getX(), (int) MouseUtil.getY());
                        if (recipeLayout != null) {
                            bookmarkOverlay.setRecipeLayout(recipeLayout);
                            bookmarkOverlay.setInfoUnderMouse(recipeInfo);
                            return recipeLayout;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isContainerTextFieldFocused(Screen screen) {
        return reflectionUtil.getFieldWithClass(screen, EditBox.class)
                .anyMatch(textField -> textField.isActive() && textField.isFocused());
    }

}
