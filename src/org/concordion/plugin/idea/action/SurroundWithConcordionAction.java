package org.concordion.plugin.idea.action;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.editor.actions.TextComponentEditorAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.ConcordionSpecTypeStrategy;
import org.concordion.plugin.idea.action.surround.ConcordionHtmlSurrounder;
import org.concordion.plugin.idea.action.surround.ConcordionMdSurrounder;
import org.concordion.plugin.idea.action.surround.ConcordionSurrounder;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionEditorUtil.findFileFrom;
import static org.concordion.plugin.idea.ConcordionSpecTypeStrategy.create;

public class SurroundWithConcordionAction extends TextComponentEditorAction {

    private static final ConcordionSpecTypeStrategy<ConcordionSurrounder> SURROUNDERS =
            create(new ConcordionHtmlSurrounder(), new ConcordionMdSurrounder());

    public SurroundWithConcordionAction() {
        super(new SurroundWithConcordionActionHandler());
    }

    private static final class SurroundWithConcordionActionHandler extends EditorWriteActionHandler {
        @Override
        public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
            runForCaret(editor, caret, safeCaret -> {

                PsiFile file = findFileFrom(editor);
                if (file == null) {
                    return;
                }
                ConcordionSurrounder surrounder = SURROUNDERS.forSpecIn(file);
                TextRange selection = new TextRange(safeCaret.getSelectionStart(), safeCaret.getSelectionEnd());

                if (surrounder == null
                        || selection.isEmpty()) {
                    return;
                }

                safeCaret.removeSelection();

                Document document = editor.getDocument();
                String surroundedText = surrounder.surround(document.getText(selection));
                document.replaceString(selection.getStartOffset(), selection.getEndOffset(), surroundedText);

                safeCaret.getCaretModel().moveToOffset(selection.getStartOffset() + surrounder.caretOffsetInSurroundedText(surroundedText));
            });
        }
    }

    private static void runForCaret(Editor editor, Caret caret, CaretAction action) {
        if (caret == null) {
            editor.getCaretModel().runForEachCaret(action);
        } else {
            action.perform(caret);
        }
    }
}
