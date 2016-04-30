package org.concordion.plugin.idea.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.lang.psi.ConcordionEmbeddedCommand;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.concordion.plugin.idea.settings.ConcordionSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.concordion.plugin.idea.ConcordionCommand.commands;
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
                "Command is used in wrong case"
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
}
