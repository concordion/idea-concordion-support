package org.concordion.plugin.idea.fixtures;

import com.intellij.psi.*;
import org.concordion.plugin.idea.action.quickfix.factories.GroovyFixtureMemberFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.GroovyLanguage;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;

public class ConcordionGroovyTestFixture extends AbstractConcordionTestFixture<GrReferenceExpression> {

    public ConcordionGroovyTestFixture() {
        super(GroovyLanguage.INSTANCE, "groovy", GrReferenceExpression.class, new GroovyFixtureMemberFactory());
    }

    @Nullable
    @Override
    protected String qualifiedReference(@NotNull GrReferenceExpression codeReference) {
        if (codeReference.getQualifier() == null || codeReference.getQualifier().getNominalType() == null) {
            return null;
        }
        PsiType nominalType = codeReference.getQualifier().getNominalType();
        if (isClassType(nominalType)) {
            nominalType = classParameterType(nominalType, codeReference.getProject());
        }
        return nominalType.getCanonicalText();
    }
}
