package org.concordion.plugin.idea.annotator;

import org.concordion.plugin.idea.ConcordionExpressionElementPattern;
import org.concordion.plugin.idea.action.quickfix.CreateFromConcordionUsage;
import org.concordion.plugin.idea.lang.psi.ConcordionMember;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionElementPattern.TEST_FIXTURE;
import static org.concordion.plugin.idea.ConcordionExpressionPatterns.concordionExpressionElement;

public abstract class UnresolvedMemberAnnotator<T extends ConcordionMember> implements Annotator {

    private final ConcordionExpressionElementPattern.Capture<T> unresolvedMember;

    public UnresolvedMemberAnnotator(Class<T> member) {
        this.unresolvedMember = concordionExpressionElement(member).withFoundTestFixture().withResolved(false);;
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {

        ProcessingContext context = new ProcessingContext();
        if (unresolvedMember.accepts(element, context)) {

            PsiClass testFixture = context.get(TEST_FIXTURE);

            holder
                    .createErrorAnnotation(element, createDescription((T) element))
                    .registerFix(createFix(testFixture, (T) element));

        }
    }

    protected abstract String createDescription(@NotNull T element);

    protected abstract CreateFromConcordionUsage<T> createFix(@NotNull PsiClass testFixture, @NotNull T element);
}
