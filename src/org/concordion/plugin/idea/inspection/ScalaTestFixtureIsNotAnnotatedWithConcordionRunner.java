package org.concordion.plugin.idea.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.*;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes;
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScClass;

import static org.concordion.plugin.idea.patterns.ConcordionElementPattern.PARENT_OF_THE_PARENT_OF_THE_PARENT;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class ScalaTestFixtureIsNotAnnotatedWithConcordionRunner extends LocalInspectionTool {

    private static final ConcordionElementPattern.Capture<PsiElement> MISCONFIGURED_TEST_FIXTURE =
            concordionElement(ScalaTokenTypes.tIDENTIFIER)
                    .withParent(ScClass.class)
                    .withSuperParent(PARENT_OF_THE_PARENT_OF_THE_PARENT, PsiFile.class)
                    .withFoundSpecOfAnyType()
                    .withTestFixtureConfigured(false);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new ConcordionInspectionVisitor<>(
                MISCONFIGURED_TEST_FIXTURE,
                holder,
                ConcordionBundle.message("concordion.inspection.fixture_not_annotated_with_runner", "@RunWith(classOf[ConcordionRunner])"),
                null
        );
    }
}
