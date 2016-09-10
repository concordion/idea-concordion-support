package org.concordion.plugin.idea.action.quickfix;

import org.concordion.plugin.idea.action.quickfix.factories.MemberCreationParameters;
import org.concordion.plugin.idea.lang.psi.ConcordionMethod;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.findString;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static org.concordion.plugin.idea.action.quickfix.factories.MemberCreationParameters.memberIn;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.memberFactory;

public class CreateMethodFromConcordionUsage extends CreateFromConcordionUsage<ConcordionMethod> {

    public CreateMethodFromConcordionUsage(@Nullable PsiClass fixture, @NotNull ConcordionMethod source) {
        super(fixture, source, "concordion.action.create_method_from_usage");
    }

    @Override
    protected PsiMember createdMember(@NotNull Project project) {

        PsiType defaultType = findString(project);

        MemberCreationParameters member = memberIn(fixture)
                .withSignature(source.getName(), defaultType);

        int index = 0;
        for (ConcordionOgnlExpressionStart argumentExpression : source.getArguments().getOgnlExpressionStartList()) {
            String nameInExpression = nameInExpression(argumentExpression);
            PsiType argumentType = typeOfExpression(argumentExpression);

            member.withParameter(
                    nameInExpression != null ? nameInExpression : "param" + index,
                    argumentType == null || DYNAMIC.equals(argumentType) ? defaultType : argumentType
            );

            index++;
        }

        return memberFactory(fixture).createMethod(member);
    }
}
