package org.concordion.plugin.idea.settings;

import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public enum ConcordionFilesRefactoring {

    NONE,
    SELECTED_ONLY,
    BOTH;

    @NotNull
    public static ConcordionFilesRefactoring fromDialogReturnCode(int returnCode) {
        switch (returnCode) {
            case Messages.YES:
                return BOTH;
            case Messages.NO:
                return SELECTED_ONLY;
            default:
                return NONE;
        }
    }
}
