package org.concordion.plugin.idea.action.quickfix;

import org.concordion.plugin.idea.lang.psi.ConcordionField;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.findString;
import static org.concordion.plugin.idea.action.quickfix.factories.MemberCreationParameters.memberIn;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.memberFactory;

public class CreateFieldFromConcordionUsage extends CreateFromConcordionUsage<ConcordionField> {

    public CreateFieldFromConcordionUsage(@Nullable PsiClass fixture, @NotNull ConcordionField source) {
        super(fixture, source, "concordion.action.create_field_from_usage");
    }

    @Override
    protected PsiMember createdMember(@NotNull Project project) {

        return memberFactory(fixture).createField(
                memberIn(fixture).withSignature(source.getName(), findString(project))
        );
    }
}
