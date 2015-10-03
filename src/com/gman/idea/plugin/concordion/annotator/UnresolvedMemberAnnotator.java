package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.ConcordionExpressionElementPattern;
import com.gman.idea.plugin.concordion.action.quickfix.CreateFieldFromConcordionUsage;
import com.gman.idea.plugin.concordion.action.quickfix.CreateFromConcordionUsage;
import com.gman.idea.plugin.concordion.action.quickfix.CreateMethodFromConcordionUsage;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMember;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.ConcordionElementPattern.TEST_FIXTURE;
import static com.gman.idea.plugin.concordion.ConcordionExpressionPatterns.concordionExpressionElement;

public class UnresolvedMemberAnnotator implements Annotator {

    private static final ConcordionExpressionElementPattern.Capture<ConcordionMember> UNRESOLVED_MEMBER =
            concordionExpressionElement(ConcordionMember.class).withFoundTestFixture().withResolved(false);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {

        ProcessingContext context = new ProcessingContext();
        if (UNRESOLVED_MEMBER.accepts(element, context)) {

            PsiClass testFixture = context.get(TEST_FIXTURE);

            holder
                    .createErrorAnnotation(element, "Member not found")
                    .registerFix(createFix(testFixture, (ConcordionMember) element));

        }
    }

    private CreateFromConcordionUsage<? extends ConcordionMember> createFix(PsiClass testFixture, ConcordionMember member) {
        if (member instanceof ConcordionField) {
            return new CreateFieldFromConcordionUsage(testFixture, (ConcordionField) member);
        }
        if (member instanceof ConcordionMethod) {
            return new CreateMethodFromConcordionUsage(testFixture, (ConcordionMethod) member);
        }
        throw new IllegalArgumentException("Unexpected concordion member");
    }
}
