package org.concordion.plugin.idea.annotator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import org.concordion.plugin.idea.action.quickfix.CreateFromConcordionUsage;
import org.concordion.plugin.idea.action.quickfix.CreateMethodFromConcordionUsage;
import org.concordion.plugin.idea.lang.psi.ConcordionMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static org.concordion.plugin.idea.ConcordionPsiUtils.DYNAMIC;
import static org.concordion.plugin.idea.ConcordionPsiUtils.typeOfExpressions;

public class UnresolvedMethodAnnotator extends UnresolvedMemberAnnotator<ConcordionMethod> {

    public UnresolvedMethodAnnotator() {
        super(ConcordionMethod.class);
    }

    @Override
    protected String createDescription(@NotNull ConcordionMethod element) {
        PsiClass containingClass = element.getContainingClass();
        Iterator<PsiType> argumentTypes = typeOfExpressions(element.getArguments().getOgnlExpressionStartList()).iterator();

        StringBuilder message = new StringBuilder().append("Method ").append(element.getName()).append('(');
        while(argumentTypes.hasNext()) {
            message.append(describeArgumentType(argumentTypes.next()));
            if (argumentTypes.hasNext()) {
                message.append(", ");
            }
        }
        message.append(") is not found");
        if (containingClass != null) {
            message.append(" in class ").append(containingClass.getName());
        }
        return message.toString();
    }

    private String describeArgumentType(PsiType argumentType) {
        return argumentType == null || argumentType == DYNAMIC ? "?" : argumentType.getPresentableText();
    }

    @Override
    protected CreateFromConcordionUsage<ConcordionMethod> createFix(@NotNull PsiClass testFixture, @NotNull ConcordionMethod element) {
        return new CreateMethodFromConcordionUsage(testFixture, element);
    }
}
