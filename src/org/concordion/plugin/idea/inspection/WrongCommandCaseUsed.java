package org.concordion.plugin.idea.inspection;

import com.google.common.collect.ImmutableMap;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.lang.ConcordionElementFactory;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.lang.psi.ConcordionEmbeddedCommand;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.concordion.plugin.idea.settings.ConcordionSettingsState;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.concordion.plugin.idea.ConcordionCommand.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static org.concordion.plugin.idea.ConcordionSpecType.HTML;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class WrongCommandCaseUsed extends LocalInspectionTool implements ConcordionSettingsListener {

    public WrongCommandCaseUsed() {
        registerListener();
    }

    @Override
    public void settingsChanged(@NotNull ConcordionSettingsState newSettings) {
        ConcordionCommandsCaseType caseType = newSettings.getCommandsCaseType();

        pattern = caseType == ConcordionCommandsCaseType.BOTH ? null : newPattern(
                commands()
                        .filter(command -> !command.fitsForCaseType(caseType))
                        .map(ConcordionCommand::text)
                        .collect(toSet())
        );
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {

        return new ConcordionInspectionVisitor<PsiElement>(
                concordionElement(),
                holder,
                "Command is used in wrong case",
                new FixCommandCaseQuickFix()
        ) {
            @Override
            protected boolean problematic(PsiElement element) {
                return pattern != null && pattern.accepts(element);
            }
        };
    }

    @Nullable
    private ConcordionElementPattern.Capture<PsiElement> pattern;

    private ConcordionElementPattern.Capture<PsiElement> newPattern(Set<String> wrongCaseExamples) {
        return concordionElement().andOr(
                concordionElement().withLanguage(ConcordionLanguage.INSTANCE).withParent(ConcordionEmbeddedCommand.class),
                concordionElement().withLanguage(XMLLanguage.INSTANCE).withParent(XmlAttribute.class).withConfiguredSpecOfType(HTML).withFoundTestFixture().withConcordionXmlAttribute()
        ).withCommandTextIn(wrongCaseExamples);
    }

    private static final String ACTION_NAME = "Fix command case";

    private static final Map<String, ConcordionCommand> CASE_FIXER = ImmutableMap.<String, ConcordionCommand>builder()

            .put(ASSERT_EQUALS_CAMEL.text(), ASSERT_EQUALS_SPINAL)
            .put(ASSERT_EQUALS_SPINAL.text(), ASSERT_EQUALS_CAMEL)

            .put(ASSERT_TRUE_CAMEL.text(), ASSERT_TRUE_SPINAL)
            .put(ASSERT_TRUE_SPINAL.text(), ASSERT_TRUE_CAMEL)

            .put(ASSERT_FALSE_CAMEL.text(), ASSERT_FALSE_SPINAL)
            .put(ASSERT_FALSE_SPINAL.text(), ASSERT_FALSE_CAMEL)

            .put(VERIFY_ROWS_CAMEL.text(), VERIFY_ROWS_SPINAL)
            .put(VERIFY_ROWS_SPINAL.text(), VERIFY_ROWS_CAMEL)

            .put(MATCH_STRATEGY_CAMEL.text(), MATCH_STRATEGY_SPINAL)
            .put(MATCH_STRATEGY_SPINAL.text(), MATCH_STRATEGY_CAMEL)

            .put(MATCHING_ROLE_CAMEL.text(), MATCHING_ROLE_SPINAL)
            .put(MATCHING_ROLE_SPINAL.text(), MATCHING_ROLE_CAMEL)

            .build();

    private static final class FixCommandCaseQuickFix implements LocalQuickFix {

        @Nls
        @NotNull
        @Override
        public String getName() {
            return ACTION_NAME;
        }

        @Nls
        @NotNull
        @Override
        public String getFamilyName() {
            return ACTION_NAME;
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
            PsiElement psiElement = problemDescriptor.getPsiElement();
            String text = commandTextOf(psiElement);
            String newText = CASE_FIXER.get(text).prefixedText("c");//TODO compute used prefix

            if (psiElement.getParent() instanceof XmlAttribute) {
                ((XmlAttribute) psiElement.getParent()).setName(newText);
            }
            if (psiElement.getParent() instanceof ConcordionEmbeddedCommand) {
                ASTNode owner = psiElement.getParent().getNode();
                owner.replaceChild(
                        owner.getFirstChildNode(),
                        ConcordionElementFactory.createEmbeddedCommand(project, newText).getNode()
                );
            }
        }
    }
}
