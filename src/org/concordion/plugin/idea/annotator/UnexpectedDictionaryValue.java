package org.concordion.plugin.idea.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.concordion.plugin.idea.*;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionCommand.EXECUTE;
import static org.concordion.plugin.idea.ConcordionPsiUtils.commandOf;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class UnexpectedDictionaryValue implements Annotator {

    private static final ConcordionElementPattern.Capture<PsiElement> CAPTURE =
            concordionElement(ConcordionTypes.DICTIONARY);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (CAPTURE.accepts(element)) {
            ConcordionCommand command = commandOf(element).orElse(EXECUTE);

            if (command.expression()
                    || !command.dictionaryValues().contains(element.getText())) {
                holder.createErrorAnnotation(element, ConcordionBundle.message("concordion.annotator.unexpected_dictionary_value"));
            }
        }
    }
}
