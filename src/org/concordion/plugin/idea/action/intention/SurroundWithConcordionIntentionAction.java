package org.concordion.plugin.idea.action.intention;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.concordion.plugin.idea.ConcordionSpecTypeStrategy;
import org.concordion.plugin.idea.action.intention.surround.ConcordionHtmlSurrounder;
import org.concordion.plugin.idea.action.intention.surround.ConcordionMdSurrounder;
import org.concordion.plugin.idea.action.intention.surround.ConcordionSurrounder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionSpecType.specConfiguredInFile;
import static org.concordion.plugin.idea.ConcordionSpecTypeStrategy.create;

public class SurroundWithConcordionIntentionAction extends BaseIntentionAction {

    private static final String ACTION_NAME = "Surround with Concordion expression";

    private static final ConcordionSpecTypeStrategy<ConcordionSurrounder> SURROUNDERS =
            create(new ConcordionHtmlSurrounder(), new ConcordionMdSurrounder());

    public SurroundWithConcordionIntentionAction() {
        setText(ACTION_NAME);
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return ACTION_NAME;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return specConfiguredInFile(file);
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        editor.getCaretModel().runForEachCaret(caret -> {
            ConcordionSurrounder surrounder = SURROUNDERS.forSpecIn(file);
            TextRange selection = new TextRange(caret.getSelectionStart(), caret.getSelectionEnd());

            if (surrounder == null
                    || selection.isEmpty()) {
                return;
            }

            caret.removeSelection();

            Document document = editor.getDocument();
            String surroundedText = surrounder.surround(document.getText(selection));
            document.replaceString(selection.getStartOffset(), selection.getEndOffset(), surroundedText);

            caret.getCaretModel().moveToOffset(selection.getStartOffset() + surrounder.caretOffsetInSurroundedText(surroundedText));
        });
    }
}
