package com.gman.idea.plugin.concordion.autocomplete;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.isConcordionHtmlSpec;

public class ConcordionHtmlSpec extends PatternCondition<PsiElement> {

    public static final ConcordionHtmlSpec INSTANCE = new ConcordionHtmlSpec();

    public ConcordionHtmlSpec() {
        super(ConcordionHtmlSpec.class.getSimpleName());
    }

    @Override
    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
        return isConcordionHtmlSpec(element.getContainingFile());
    }
}
