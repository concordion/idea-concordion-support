package org.concordion.plugin.idea.settings;

import com.google.common.base.Objects;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "ConcordionSettings",
        storages = @Storage(file = "concordion_settings.xml")
)
public class ConcordionSettings implements PersistentStateComponent<ConcordionSettings.State> {

    public static ConcordionSettings getInstance() {
        return ServiceManager.getService(ConcordionSettings.class);
    }

    @NotNull
    private State state = new State();

    @NotNull
    public State currentState() {
        return state;
    }

    public void updateState(@NotNull ConcordionSettings.State state) {
        this.state = state;
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

        @NotNull
        public ConcordionCommandsCompletionType completionType = ConcordionCommandsCompletionType.BOTH;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return completionType == ((State) o).completionType;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(completionType);
        }
    }
}
