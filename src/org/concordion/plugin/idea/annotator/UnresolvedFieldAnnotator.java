package org.concordion.plugin.idea.annotator;

import com.intellij.psi.PsiClass;
import org.concordion.plugin.idea.*;
import org.concordion.plugin.idea.action.quickfix.CreateFieldFromConcordionUsage;
import org.concordion.plugin.idea.action.quickfix.CreateFromConcordionUsage;
import org.concordion.plugin.idea.lang.psi.*;
import org.jetbrains.annotations.NotNull;

public class UnresolvedFieldAnnotator extends UnresolvedMemberAnnotator<ConcordionField> {

    public UnresolvedFieldAnnotator() {
        super(ConcordionField.class);
    }

    @Override
    protected String createDescription(@NotNull ConcordionField element) {
        return ConcordionBundle.message("concordion.annotator.field_not_found", element.getName(), containingClassName(element));
    }

    @Override
    protected CreateFromConcordionUsage<ConcordionField> createFix(@NotNull PsiClass testFixture, @NotNull ConcordionField element) {
        return new CreateFieldFromConcordionUsage(testFixture, element);
    }
}
