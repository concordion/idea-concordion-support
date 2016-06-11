package org.concordion.plugin.idea.fixtures;

import org.concordion.plugin.idea.action.quickfix.factories.GroovyFixtureMemberFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.GroovyLanguage;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

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
        return codeReference.getQualifier().getNominalType().getCanonicalText();
    }
}
