package org.concordion.plugin.idea.fixtures;

import com.intellij.lang.Language;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import org.concordion.plugin.idea.action.quickfix.factories.ScalaFixtureMemberFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.scala.lang.psi.api.base.ScStableCodeReferenceElement;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScArgumentExprList;

import static com.intellij.psi.util.PsiTreeUtil.*;

public class ConcordionScalaTestFixture extends AbstractConcordionTestFixture<ScStableCodeReferenceElement> {

    public ConcordionScalaTestFixture() {
        super(Language.findLanguageByID("Scala"), "scala", ScStableCodeReferenceElement.class, new ScalaFixtureMemberFactory());
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
}
