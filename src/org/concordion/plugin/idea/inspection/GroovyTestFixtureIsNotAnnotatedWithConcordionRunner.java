package org.concordion.plugin.idea.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.concordion.plugin.idea.*;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.GrClassDefinition;

import static org.concordion.plugin.idea.patterns.ConcordionElementPattern.PARENT_OF_THE_PARENT;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class GroovyTestFixtureIsNotAnnotatedWithConcordionRunner extends LocalInspectionTool {

    private static final ConcordionElementPattern.Capture<PsiElement> MISCONFIGURED_TEST_FIXTURE =
            concordionElement(GroovyTokenTypes.mIDENT)
                    .withParent(GrClassDefinition.class)
                    .withSuperParent(PARENT_OF_THE_PARENT, PsiFile.class)
                    .withFoundSpecOfAnyType()
                    .withTestFixtureConfigured(false);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new ConcordionInspectionVisitor<>(
                MISCONFIGURED_TEST_FIXTURE,
                holder,
                ConcordionBundle.message("concordion.inspection.fixture_not_annotated_with_runner", "@RunWith(ConcordionRunner.class)"),
                null
        );
    }
}
