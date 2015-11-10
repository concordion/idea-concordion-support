package org.concordion.plugin.idea.annotator;

import com.intellij.psi.PsiClass;
import org.concordion.plugin.idea.action.quickfix.CreateFieldFromConcordionUsage;
import org.concordion.plugin.idea.action.quickfix.CreateFromConcordionUsage;
import org.concordion.plugin.idea.lang.psi.ConcordionField;
import org.jetbrains.annotations.NotNull;

public class UnresolvedFieldAnnotator extends UnresolvedMemberAnnotator<ConcordionField> {

    public UnresolvedFieldAnnotator() {
        super(ConcordionField.class);
    }

    @Override
    protected String createDescription(@NotNull ConcordionField element) {
        PsiClass containingClass = element.getContainingClass();

        StringBuilder message = new StringBuilder().append("Field ").append(element.getName()).append(" is not found");
        if (containingClass != null) {
            message.append(" in class ").append(containingClass.getName());
        }
        return message.toString();
    }

    @Override
    protected CreateFromConcordionUsage<ConcordionField> createFix(@NotNull PsiClass testFixture, @NotNull ConcordionField element) {
        return new CreateFieldFromConcordionUsage(testFixture, element);
    }
}
