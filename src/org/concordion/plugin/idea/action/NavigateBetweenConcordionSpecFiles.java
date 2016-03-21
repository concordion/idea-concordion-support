package org.concordion.plugin.idea.action;

import org.concordion.plugin.idea.ConcordionNavigationService;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

import static org.concordion.plugin.idea.ConcordionEditorUtil.findFileFrom;

public class NavigateBetweenConcordionSpecFiles extends EditorAction {

    public NavigateBetweenConcordionSpecFiles() {
        super(new NavigateBetweenConcordionSpecFilesHandler());
    }

    private static final class NavigateBetweenConcordionSpecFilesHandler extends EditorActionHandler {
        @Override
        protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {

            ConcordionNavigationService.getInstance(editor.getProject()).navigateToPairedFile(findFileFrom(editor));
        }
    }
}
