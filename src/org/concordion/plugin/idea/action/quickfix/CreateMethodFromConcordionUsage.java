package org.concordion.plugin.idea.action.quickfix;

import org.concordion.plugin.idea.lang.psi.ConcordionMethod;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.PsiModifier.PUBLIC;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.findObject;
import static org.concordion.plugin.idea.ConcordionPsiUtils.DYNAMIC;
import static org.concordion.plugin.idea.ConcordionPsiUtils.typeOfExpressions;

public class CreateMethodFromConcordionUsage extends CreateFromConcordionUsage<ConcordionMethod> {

    public CreateMethodFromConcordionUsage(@Nullable PsiClass javaRunner, @NotNull ConcordionMethod source) {
        super(javaRunner, source, "Create method from usage");
    }

    @Override
    protected PsiMember createdMember(Project project, PsiElementFactory factory) {
        PsiType defaultType = findObject(project);

        PsiMethod createdMethod = factory.createMethod(source.getName(), defaultType);
        createdMethod.getModifierList().setModifierProperty(PUBLIC, true);

        int parameterNumber = 0;
        for (PsiType psiType : typeOfExpressions(source.getArguments().getOgnlExpressionStartList())) {
            createdMethod.getParameterList().add(factory.createParameter(
                    "param" + parameterNumber,
                    psiType == null || psiType == DYNAMIC ? defaultType : psiType
            ));
            parameterNumber++;
        }
        createdMethod.getBody().add(factory.createStatementFromText("return null;", null));

        return (PsiMethod) javaRunner.add(createdMethod);
    }

}
