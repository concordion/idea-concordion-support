package org.concordion.plugin.idea.settings;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ConcordionSettingsListener extends EventListener {

    void settingsChanged(@NotNull ConcordionSettingsState newSettings);

    default void registerListener() {
        ConcordionSettings.getInstance().addListener(this);
    }
}
