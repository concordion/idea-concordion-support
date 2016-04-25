package org.concordion.plugin.idea.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConcordionConfigurable implements SearchableConfigurable {

    @Nullable
    private ConcordionSettingsForm settingsForm;

    @NotNull
    @Override
    public String getId() {
        return "Settings.Concordion";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Concordion";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return getSettingsForm().settingsPanel();
    }

    @Override
    public boolean isModified() {
        return !ConcordionSettings.getInstance().getState().equals(getSettingsForm().createSettings());
    }

    @Override
    public void apply() throws ConfigurationException {
        ConcordionSettings.getInstance().updateState(getSettingsForm().createSettings());
    }

    @Override
    public void reset() {
        getSettingsForm().setSettings(ConcordionSettings.getInstance().getState());
    }

    @Override
    public void disposeUIResources() {
        settingsForm = null;
    }

    @NotNull
    private ConcordionSettingsForm getSettingsForm() {
        if (settingsForm == null) {
            settingsForm = new ConcordionSettingsForm();
        }
        return settingsForm;
    }
}
