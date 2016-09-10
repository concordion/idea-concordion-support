package org.concordion.plugin.idea.refactoring;

import com.intellij.CommonBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.concordion.plugin.idea.*;
import org.concordion.plugin.idea.settings.ConcordionFilesRefactoring;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ConcordionRefactoringDialogs {

    @NotNull
    public static ConcordionFilesRefactoring renamePaired(@NotNull ConcordionPairedFileType file, @NotNull ConcordionFilesRefactoring rememberedState) {
        return modifyPairedFile(
                ConcordionBundle.message("concordion.refactoring.rename_paired." + file.name()),
                rememberedState,
                ConcordionSettings.getInstance()::setRenamePairs
        );
    }

    @NotNull
    public static ConcordionFilesRefactoring deletePaired(@NotNull ConcordionPairedFileType file, @NotNull ConcordionFilesRefactoring rememberedState) {
        return modifyPairedFile(
                ConcordionBundle.message("concordion.refactoring.delete_paired." + file.name()),
                rememberedState,
                ConcordionSettings.getInstance()::setDeletePairs
        );
    }

    @NotNull
    private static ConcordionFilesRefactoring modifyPairedFile(
            @NotNull String message,
            @NotNull ConcordionFilesRefactoring rememberedState,
            @NotNull Consumer<ConcordionFilesRefactoring> rememberedStateUpdater
    ) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return ConcordionFilesRefactoring.BOTH;
        }

        if (rememberedState != ConcordionFilesRefactoring.ASK) {
            return rememberedState;
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
            if (!toBeShown) {
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
