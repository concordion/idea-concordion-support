package org.concordion.plugin.idea.settings;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;

public class ConcordionSettingsState {

    @NotNull
    private ConcordionCommandsCaseType commandsCaseType = ConcordionCommandsCaseType.BOTH;

    @NotNull
    public ConcordionCommandsCaseType getCommandsCaseType() {
        return commandsCaseType;
    }

    public void setCommandsCaseType(@NotNull ConcordionCommandsCaseType commandsCaseType) {
        this.commandsCaseType = commandsCaseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return commandsCaseType == ((ConcordionSettingsState) o).commandsCaseType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commandsCaseType);
    }
}
