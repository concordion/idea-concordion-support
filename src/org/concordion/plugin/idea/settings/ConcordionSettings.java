package org.concordion.plugin.idea.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;

@State(
        name = "ConcordionSettings",
        storages = @Storage(file = "concordion.xml")
)
public class ConcordionSettings implements PersistentStateComponent<ConcordionSettings> {

    public static ConcordionSettings getInstance() {
        return ServiceManager.getService(ConcordionSettings.class);
    }

    @NotNull private ConcordionCommandsCaseType commandsCaseType = ConcordionCommandsCaseType.BOTH;
    @NotNull private ConcordionFilesRefactoring renamePairs = ConcordionFilesRefactoring.ASK;
    @NotNull private ConcordionFilesRefactoring movePairs = ConcordionFilesRefactoring.ASK;
    @NotNull private ConcordionFilesRefactoring deletePairs = ConcordionFilesRefactoring.ASK;

    @NotNull private final Collection<WeakReference<ConcordionSettingsListener>> listeners = new ArrayList<>();

    public void addListener(@NotNull ConcordionSettingsListener listener) {
        listeners.add(new WeakReference<>(listener));
        listener.settingsChanged(this);
    }

    @NotNull
    @Override
    public ConcordionSettings getState() {
        return this;
    }

    @Override
    public void loadState(ConcordionSettings state) {
        XmlSerializerUtil.copyBean(state, this);
        notifyListeners();
    }

    private void notifyListeners() {
        for (WeakReference<ConcordionSettingsListener> listener : listeners) {
            ConcordionSettingsListener listenerRef = listener.get();
            if (listenerRef != null) {
                listenerRef.settingsChanged(this);
            }
        }
    }

    @NotNull
    public ConcordionCommandsCaseType getCommandsCaseType() {
        return commandsCaseType;
    }

    public void setCommandsCaseType(@NotNull ConcordionCommandsCaseType commandsCaseType) {
        this.commandsCaseType = commandsCaseType;
        notifyListeners();
    }

    @NotNull
    public ConcordionFilesRefactoring getRenamePairs() {
        return renamePairs;
    }

    public void setRenamePairs(@NotNull ConcordionFilesRefactoring renamePairs) {
        this.renamePairs = renamePairs;
        notifyListeners();
    }

    @NotNull
    public ConcordionFilesRefactoring getMovePairs() {
        return movePairs;
    }

    public void setMovePairs(@NotNull ConcordionFilesRefactoring movePairs) {
        this.movePairs = movePairs;
        notifyListeners();
    }

    @NotNull
    public ConcordionFilesRefactoring getDeletePairs() {
        return deletePairs;
    }

    public void setDeletePairs(@NotNull ConcordionFilesRefactoring deletePairs) {
        this.deletePairs = deletePairs;
        notifyListeners();
    }
}
