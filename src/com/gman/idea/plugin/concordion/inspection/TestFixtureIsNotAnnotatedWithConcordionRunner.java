package com.gman.idea.plugin.concordion.inspection;

import com.gman.idea.plugin.concordion.ConcordionElementPattern;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.ConcordionPatterns.concordionElement;

public class TestFixtureIsNotAnnotatedWithConcordionRunner extends LocalInspectionTool {

    private static final ConcordionElementPattern.Capture<PsiIdentifier> MISCONFIGURED_TEST_FIXTURE =
            concordionElement(PsiIdentifier.class)
                    .withParent(PsiClass.class)
                    .withSuperParent(2, PsiFile.class)
                    .withFoundHtmlSpec().withTestFixtureConfigured(false);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);

                if (MISCONFIGURED_TEST_FIXTURE.accepts(element)) {
                    holder.registerProblem(element, "Test fixture is not annotated with @RunWith(ConcordionRunner.class)");
                }
            }
        };
    }
}
