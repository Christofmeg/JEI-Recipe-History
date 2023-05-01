package com.christofmeg.jeirecipehistory.platform.services;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public interface IPlatformConfigHelper {


    /**
     * Used in "JEI Recipe History Config" button that opens client-config if Configured is installed
     */
    Optional<Screen> getConfigScreen();

    /**
     * Used in "JEI Recipe History Config" button that opens client-config if Configured is installed
     */
    Component getMissingConfigScreenMessage();
}