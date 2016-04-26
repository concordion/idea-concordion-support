package org.concordion.plugin.idea.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;

@State(
        name = "ConcordionSettings",
        storages = @Storage(file = "concordion_settings.xml")
)
public class ConcordionSettings implements PersistentStateComponent<ConcordionSettingsState> {

    public static ConcordionSettings getInstance() {
        return ServiceManager.getService(ConcordionSettings.class);
    }

    @NotNull
    private final Collection<WeakReference<ConcordionSettingsListener>> listeners = new ArrayList<>();

    @NotNull
    private ConcordionSettingsState state = new ConcordionSettingsState();

    public void updateState(@NotNull ConcordionSettingsState state) {
        this.state = state;
        notifyListeners();
    }

    public void addListener(@NotNull ConcordionSettingsListener listener) {
        listeners.add(new WeakReference<>(listener));
        listener.settingsChanged(state);
    }

    @NotNull
    @Override
    public ConcordionSettingsState getState() {
        return state;
    }

    @Override
    public void loadState(ConcordionSettingsState state) {
        this.state = state;
        notifyListeners();
    }

    private void notifyListeners() {
        for (WeakReference<ConcordionSettingsListener> listener : listeners) {
            ConcordionSettingsListener listenerRef = listener.get();
            if (listenerRef != null) {
                listenerRef.settingsChanged(state);
            }
        }
    }
}
