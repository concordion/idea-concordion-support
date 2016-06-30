package org.concordion.plugin.idea.refactoring;

import com.intellij.CommonBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.concordion.plugin.idea.settings.ConcordionFilesRefactoring;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConcordionRefactoringDialogs {

    @NotNull
    public static ConcordionFilesRefactoring renamePairedFile() {
        return modifyPairedFile(
                "Paired concordion file detected. Should it be renamed as well?",
                ConcordionSettings.getInstance()::getRenamePairs,
                ConcordionSettings.getInstance()::setRenamePairs
        );
    }

    @NotNull
    public static ConcordionFilesRefactoring deletePairedFile() {
        return modifyPairedFile(
                "Paired concordion file detected. Should it be deleted as well?",
                ConcordionSettings.getInstance()::getRemovePairs,
                ConcordionSettings.getInstance()::setRemovePairs
        );
    }

    @NotNull
    private static ConcordionFilesRefactoring modifyPairedFile(
            @NotNull String message,
            @NotNull Supplier<ConcordionFilesRefactoring> rememberedState,
            @NotNull Consumer<ConcordionFilesRefactoring> rememberedStateUpdater
    ) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return ConcordionFilesRefactoring.BOTH;
        }

        ConcordionFilesRefactoring movePairs = rememberedState.get();
        if (movePairs != ConcordionFilesRefactoring.ASK) {
            return movePairs;
        }

        return ConcordionFilesRefactoring.fromDialogReturnCode(Messages.showYesNoDialog(
                message,
                "Concordion",
                "Yes",
                "No",
                Messages.getQuestionIcon(),
                new ConcordionPairedFileDoNotAskOption(rememberedStateUpdater)));
    }

    private static final class ConcordionPairedFileDoNotAskOption implements DialogWrapper.DoNotAskOption {

        private final @NotNull Consumer<ConcordionFilesRefactoring> rememberedStateUpdater;

        public ConcordionPairedFileDoNotAskOption(@NotNull Consumer<ConcordionFilesRefactoring> rememberedStateUpdater) {
            this.rememberedStateUpdater = rememberedStateUpdater;
        }

        @Override
        public void setToBeShown(boolean toBeShown, int exitCode) {
            if (exitCode != Messages.CANCEL) {
                rememberedStateUpdater.accept(ConcordionFilesRefactoring.fromDialogReturnCode(exitCode));
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
