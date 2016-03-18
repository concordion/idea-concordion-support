package org.concordion.plugin.idea.action;

import org.concordion.plugin.idea.ConcordionNavigationService;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.psi.PsiFile;

import static org.concordion.plugin.idea.ConcordionEditorUtil.findFileFrom;

public class NavigateBetweenConcordionSpecFiles extends EditorAction {

    public NavigateBetweenConcordionSpecFiles() {
        super(new NavigateBetweenConcordionSpecFilesHandler());
    }

    private static final class NavigateBetweenConcordionSpecFilesHandler extends EditorActionHandler {
        @Override
        protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {

            PsiFile file = findFileFrom(editor);
            if (file == null) {
                return;
            }

            PsiFile correspondingFile = ConcordionNavigationService.getInstance(editor.getProject()).correspondingPairedFile(file);
            if (correspondingFile == null) {
                return;
            }

            correspondingFile.navigate(true);
        }
    }
}
