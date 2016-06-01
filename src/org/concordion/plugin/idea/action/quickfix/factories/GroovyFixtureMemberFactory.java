package org.concordion.plugin.idea.action.quickfix.factories;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;

import static com.intellij.psi.PsiModifier.PUBLIC;

public class GroovyFixtureMemberFactory implements ConcordionFixtureMemberFactory {

    @NotNull
    @Override
    public PsiField createField(@NotNull MemberCreationParameters parameters) {

        GroovyPsiElementFactory factory = GroovyPsiElementFactory.getInstance(parameters.project());

        PsiField field = factory.createField(parameters.member.name, parameters.member.type);
        field.getModifierList().setModifierProperty(PUBLIC, true);

        parameters.fixture.add(field);

        return field;
    }

    @NotNull
    @Override
    public PsiMethod createMethod(@NotNull MemberCreationParameters parameters) {

        GroovyPsiElementFactory factory = GroovyPsiElementFactory.getInstance(parameters.project());

        PsiMethod method = factory.createMethod(parameters.member.name, parameters.member.type);
        method.getModifierList().setModifierProperty(PUBLIC, true);
        for (MemberCreationParameters.NameAndType param : parameters.nameAndTypes) {
            method.getParameterList().add(factory.createParameter(param.name, param.type));
        }

        parameters.fixture.add(method);

        return method;
    }
}
