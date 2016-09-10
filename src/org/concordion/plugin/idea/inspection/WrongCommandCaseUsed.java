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
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import org.concordion.plugin.idea.*;
import org.concordion.plugin.idea.lang.ConcordionElementFactory;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.lang.psi.ConcordionEmbeddedCommand;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.concordion.plugin.idea.specifications.ConcordionHtmlSpecification;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static org.concordion.plugin.idea.ConcordionCommand.*;
import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.*;

public class WrongCommandCaseUsed extends LocalInspectionTool implements ConcordionSettingsListener {

    public WrongCommandCaseUsed() {
        registerListener();
    }

    @Override
    public void settingsChanged(@NotNull ConcordionSettings newState) {
        ConcordionCommandsCaseType caseType = newState.getCommandsCaseType();

        pattern = caseType == ConcordionCommandsCaseType.BOTH ? null : newPattern(caseType);
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {

        return new ConcordionInspectionVisitor<PsiElement>(
                concordionElement(),
                holder,
                ConcordionBundle.message("concordion.inspection.wrong_command_case"),
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

    private ConcordionElementPattern.Capture<PsiElement> newPattern(ConcordionCommandsCaseType caseType) {
        return concordionElement().andOr(
                concordionElement().withLanguage(ConcordionLanguage.INSTANCE).withParent(ConcordionEmbeddedCommand.class),
                concordionElement().withLanguage(XMLLanguage.INSTANCE).withParent(XmlAttribute.class).withConfiguredSpecOfType(ConcordionHtmlSpecification.INSTANCE).withFoundTestFixture().withConcordionXmlAttribute()
        ).withCommand(command -> !command.fitsForCaseType(caseType));
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
            PsiElement element = problemDescriptor.getPsiElement();
            PsiFile containingFile = getContainingFile(element);
            if (containingFile == null) {
                return;
            }

            String prefix = prefixInFile(containingFile);
            String text = commandTextOf(element);
            String newText = CASE_FIXER.get(text).prefixedText(nullToEmpty(prefix));

            if (element.getParent() instanceof XmlAttribute) {
                ((XmlAttribute) element.getParent()).setName(newText);
            }
            if (element.getParent() instanceof ConcordionEmbeddedCommand) {
                ASTNode owner = element.getParent().getNode();
                owner.replaceChild(
                        owner.getFirstChildNode(),
                        ConcordionElementFactory.createEmbeddedCommand(project, newText).getNode()
                );
            }
        }
    }
}
