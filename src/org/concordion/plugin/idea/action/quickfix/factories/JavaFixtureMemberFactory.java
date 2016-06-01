package org.concordion.plugin.idea.action.quickfix.factories;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.PsiModifier.PUBLIC;

public class JavaFixtureMemberFactory implements ConcordionFixtureMemberFactory {

    @NotNull
    @Override
    public PsiField createField(@NotNull MemberCreationParameters parameters) {

        PsiElementFactory factory = JavaPsiFacade.getElementFactory(parameters.project());

        PsiField field = factory.createField(parameters.member.name, parameters.member.type);
        field.getModifierList().setModifierProperty(PUBLIC, true);

        parameters.fixture.add(field);

        return field;
    }

    @NotNull
    @Override
    public PsiMethod createMethod(@NotNull MemberCreationParameters parameters) {

        PsiElementFactory factory = JavaPsiFacade.getElementFactory(parameters.project());

        PsiMethod method = factory.createMethod(parameters.member.name, parameters.member.type);
        method.getModifierList().setModifierProperty(PUBLIC, true);
        for (MemberCreationParameters.NameAndType param : parameters.nameAndTypes) {
            method.getParameterList().add(factory.createParameter(param.name, param.type));
        }
        method.getBody().add(factory.createStatementFromText("return null;", null));

        parameters.fixture.add(method);

        return method;
    }
}
