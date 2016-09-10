package org.concordion.plugin.idea.settings;

import com.intellij.openapi.ui.Messages;
import org.concordion.plugin.idea.*;
import org.jetbrains.annotations.NotNull;

public enum ConcordionFilesRefactoring {

    DISABLED,
    ASK,
    SELECTED_ONLY,
    BOTH;

    @Override
    public String toString() {
        return ConcordionBundle.message("concordion.settings.paired_file_action." + name());
    }

    @NotNull
    public static ConcordionFilesRefactoring fromDialogReturnCode(int returnCode) {
        switch (returnCode) {
            case Messages.YES:
                return BOTH;
            case Messages.NO:
                return SELECTED_ONLY;
            default:
                return ASK;
        }
    }
}
