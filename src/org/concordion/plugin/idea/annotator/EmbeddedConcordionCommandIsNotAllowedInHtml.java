package org.concordion.plugin.idea.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.concordion.plugin.idea.ConcordionElementPattern;
import org.concordion.plugin.idea.lang.psi.ConcordionEmbeddedCommand;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionSpecType.HTML;

public class EmbeddedConcordionCommandIsNotAllowedInHtml implements Annotator {

    public static final ConcordionElementPattern.Capture<ConcordionEmbeddedCommand> EMBEDDED_COMMAND_USAGE_IN_HTML =
            concordionElement(ConcordionEmbeddedCommand.class)
                    .withSpecOfType(HTML);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (EMBEDDED_COMMAND_USAGE_IN_HTML.accepts(element)) {
            holder.createErrorAnnotation(element, "Unexpected concordion command");
        }
    }
}
