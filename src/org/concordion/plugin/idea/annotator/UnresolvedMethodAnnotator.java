package org.concordion.plugin.idea.annotator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import org.concordion.plugin.idea.*;
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
        return ConcordionBundle.message("concordion.annotator.method_not_found", methodSignature(element), containingClassName(element));
    }

    private String methodSignature(@NotNull ConcordionMethod element) {
        Iterator<PsiType> argumentTypes = typeOfExpressions(element.getArguments().getOgnlExpressionStartList()).iterator();

        StringBuilder signature = new StringBuilder();
        signature.append(element.getName());
        signature.append('(');
        while(argumentTypes.hasNext()) {
            signature.append(describeArgumentType(argumentTypes.next()));
            if (argumentTypes.hasNext()) {
                signature.append(", ");
            }
        }
        signature.append(')');
        return signature.toString();
    }

    private String describeArgumentType(PsiType argumentType) {
        return argumentType == null || argumentType == DYNAMIC ? "?" : argumentType.getPresentableText();
    }

    @Override
    protected CreateFromConcordionUsage<ConcordionMethod> createFix(@NotNull PsiClass testFixture, @NotNull ConcordionMethod element) {
        return new CreateMethodFromConcordionUsage(testFixture, element);
    }
}
