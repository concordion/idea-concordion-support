package org.concordion.plugin.idea.action.quickfix;

import org.concordion.plugin.idea.lang.psi.ConcordionMethod;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.PsiModifier.PUBLIC;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.findString;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public class CreateMethodFromConcordionUsage extends CreateFromConcordionUsage<ConcordionMethod> {

    public CreateMethodFromConcordionUsage(@Nullable PsiClass javaRunner, @NotNull ConcordionMethod source) {
        super(javaRunner, source, "Create method from usage");
    }

    @Override
    protected PsiMember createdMember(Project project, PsiElementFactory factory) {
        PsiType defaultType = findString(project);

        PsiMethod createdMethod = factory.createMethod(source.getName(), defaultType);
        createdMethod.getModifierList().setModifierProperty(PUBLIC, true);

        int parameterIndex = 0;
        for (ConcordionOgnlExpressionStart argumentExpression : source.getArguments().getOgnlExpressionStartList()) {
            createdMethod.getParameterList().add(factory.createParameter(
                    deductName(argumentExpression, parameterIndex),
                    deductType(argumentExpression, defaultType)
            ));
            parameterIndex++;
        }

        createdMethod.getBody().add(factory.createStatementFromText("return null;", null));

        return (PsiMethod) javaRunner.add(createdMethod);
    }

    @NotNull
    private String deductName(ConcordionOgnlExpressionStart parameterExpression, int index) {
        String nameInExpression = nameInExpression(parameterExpression);
        return nameInExpression != null ? nameInExpression : "param" + index;
    }

    @NotNull
    private PsiType deductType(ConcordionOgnlExpressionStart parameterExpression, PsiType defaultType) {
        PsiType argumentType = typeOfExpression(parameterExpression);
        return argumentType == null || DYNAMIC.equals(argumentType) ? defaultType : argumentType;
    }

}
