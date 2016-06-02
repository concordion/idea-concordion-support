package org.concordion.plugin.idea.action.quickfix.factories;

import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.scala.lang.psi.api.statements.ScFunction;
import org.jetbrains.plugins.scala.lang.psi.impl.ScalaPsiElementFactory;

import java.util.stream.Collectors;

import static org.concordion.plugin.idea.ConcordionPsiUtils.getterFor;

public class ScalaFixtureMemberFactory implements ConcordionFixtureMemberFactory {

    @NotNull
    @Override
    public PsiMember createField(@NotNull MemberCreationParameters parameters) {

        return createMethod(parameters.withSignature(getterFor(parameters.member.name), parameters.member.type));
    }

    @NotNull
    @Override
    public PsiMethod createMethod(@NotNull MemberCreationParameters parameters) {

        StringBuilder methodText = new StringBuilder();

        methodText.append("def ").append(parameters.member.name);
        if (!parameters.parameters.isEmpty()) {
            methodText.append("(");
            methodText.append(parameters.parameters.stream()
                    .map(p -> p.name + ": " + p.type.getPresentableText())
                    .collect(Collectors.joining(", ")));
            methodText.append(")");
        }
        methodText.append(": ").append(parameters.member.type.getPresentableText()).append(" = ???");

        ScFunction method = ScalaPsiElementFactory.createMethodFromText(methodText.toString(), parameters.fixture.getManager());

        parameters.fixture.add(method);

        return method;
    }
}
