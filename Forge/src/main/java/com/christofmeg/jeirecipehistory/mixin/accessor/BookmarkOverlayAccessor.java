package com.christofmeg.jeirecipehistory.mixin.accessor;

import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.bookmarks.BookmarkOverlay;
import mezz.jei.input.mouse.handlers.CheatInputHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Mixin interface for accessing protected members of the BookmarkOverlay class.
 */
@Mixin(value = BookmarkOverlay.class, remap = false)
public interface BookmarkOverlayAccessor {

    /**
     * Accessor method for retrieving the contents of the bookmark overlay.
     *
     * @return The contents of the bookmark overlay.
     */
    @Accessor("contents")
    IngredientGridWithNavigation getContents();

    /**
     * Accessor method for retrieving the cheat input handler.
     *
     * @return The cheat input handler.
     */
    @Accessor("cheatInputHandler")
    CheatInputHandler getCheatInputHandler();

    /**
     * Accessor method for retrieving the bookmark button.
     *
     * @return The bookmark button.
     */
    @Accessor("bookmarkButton")
    GuiIconToggleButton getBookmarkButton();

    /**
     * Accessor method for retrieving the parent area of the bookmark overlay.
     *
     * @return The parent area of the bookmark overlay.
     */
    @Accessor("parentArea")
    ImmutableRect2i getParentArea();

}
