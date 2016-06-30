package org.concordion.plugin.idea.refactoring;

import com.intellij.CommonBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.concordion.plugin.idea.settings.ConcordionFilesRefactoring;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.jetbrains.annotations.NotNull;

public class ConcordionRefactoringDialogs {

    public static ConcordionFilesRefactoring renamePairedFile() {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return ConcordionFilesRefactoring.BOTH;
        }

        ConcordionFilesRefactoring movePairs = ConcordionSettings.getInstance().getState().getMovePairs();
        if (movePairs != ConcordionFilesRefactoring.NONE) {
            return movePairs;
        }

        return ConcordionFilesRefactoring.fromDialogReturnCode(Messages.showYesNoCancelDialog(
                "Paired concordion file detected. Should it be renamed as well?",
                "Concordion",
                "Yes",
                "No",
                "Cancel",
                Messages.getQuestionIcon(),
                new ConcordionPairedFileDoNotAskOption()));
    }

    private static final class ConcordionPairedFileDoNotAskOption implements DialogWrapper.DoNotAskOption {

        @Override
        public void setToBeShown(boolean toBeShown, int exitCode) {
            if (exitCode != Messages.CANCEL) {
                ConcordionSettings.getInstance().setMovePairs(ConcordionFilesRefactoring.fromDialogReturnCode(exitCode));
            }
        }

        @Override
        public boolean isToBeShown() {
            return true;
        }

        @Override
        public boolean canBeHidden() {
            return true;
        }

        @Override
        public boolean shouldSaveOptionsOnCancel() {
            return false;
        }

        @NotNull
        @Override
        public String getDoNotShowMessage() {
            return CommonBundle.message("dialog.options.do.not.ask");
        }
    }
}
