package org.concordion.plugin.idea.fixtures;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

public class ConcordionGroovyTestFixture extends AbstractConcordionTestFixture<GrReferenceExpression> {

    public ConcordionGroovyTestFixture() {
        super("groovy", GrReferenceExpression.class);
    }

    @Nullable
    @Override
    protected String qualifiedReference(@NotNull GrReferenceExpression codeReference) {
        if (codeReference.getQualifier() == null || codeReference.getQualifier().getNominalType() == null) {
            return null;
        }
        return codeReference.getQualifier().getNominalType().getCanonicalText();
    }

    @NotNull
    @Override
    public JVMElementFactory elementFactory(@NotNull PsiClass testFixture) {
        return GroovyPsiElementFactory.getInstance(testFixture.getProject());
    }
}
