package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.scala.lang.psi.api.base.ScStableCodeReferenceElement;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScArgumentExprList;

import java.util.List;
import java.util.Set;

import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.Namespaces.CONCORDION_EXTENSIONS;

public class ConcordionScalaTestFixture extends AbstractConcordionTestFixture<ScStableCodeReferenceElement> {

    public ConcordionScalaTestFixture() {
        super("scala", ScStableCodeReferenceElement.class);
    }

    @Nullable
    @Override
    protected String qualifiedReference(@NotNull ScStableCodeReferenceElement codeReference) {
        return codeReference.getCanonicalText();
    }

    @Nullable
    @Override
    protected PsiElement findAnnotationParameters(@NotNull PsiAnnotation annotation) {
        return findChildOfType(annotation, ScArgumentExprList.class);
    }

    @NotNull
    @Override
    public JVMElementFactory elementFactory(@NotNull PsiClass testFixture) {
        throw new UnsupportedOperationException();
    }
}
