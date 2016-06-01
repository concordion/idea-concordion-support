package org.concordion.plugin.idea.action;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.concordion.plugin.idea.ConcordionNavigationService;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NavigateBetweenConcordionSpecFiles extends EditorAction {

    public NavigateBetweenConcordionSpecFiles() {
        super(new NavigateBetweenConcordionSpecFilesHandler());
    }

    private static final class NavigateBetweenConcordionSpecFilesHandler extends EditorActionHandler {
        @Override
        protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {

            ConcordionNavigationService.getInstance(editor.getProject()).navigateToPairedFile(findFileFrom(editor));
        }

        @Nullable
        private PsiFile findFileFrom(@NotNull Editor editor) {
            VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (virtualFile == null
                    || editor.getProject() == null) {
                return null;
            }
            return PsiManager.getInstance(editor.getProject()).findFile(virtualFile);
        }
    }
}
