package org.concordion.plugin.idea.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "ConcordionSettings",
        storages = @Storage(file = "$APP_CONFIG$/concordion_settings.xml")
)
public class ConcordionSettings implements PersistentStateComponent<ConcordionSettings.State> {

    public static ConcordionSettings getInstance() {
        return ServiceManager.getService(ConcordionSettings.class);
    }

    @NotNull
    private State state = new State();

    @NotNull
    public State state() {
        return state;
    }

    @Nullable
    @Override
    public ConcordionSettings.State getState() {
        return state;
    }

    @Override
    public void loadState(ConcordionSettings.State state) {
        this.state = state;
    }

    public static final class State {

    }
}
