package org.concordion.plugin.idea.specifications;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.Language;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.concordion.plugin.idea.action.intention.surround.ConcordionMdSurrounder;
import org.concordion.plugin.idea.action.intention.surround.ConcordionSurrounder;
import org.concordion.plugin.idea.variables.ConcordionVariableInMdUsageSearcher;
import org.concordion.plugin.idea.variables.ConcordionVariableUsageSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.extensionNamespace;

public class ConcordionMdSpecification implements ConcordionSpecification {

    @NotNull
    public static final ConcordionSpecification INSTANCE = new ConcordionMdSpecification();

    @NotNull
    @Override
    public Language language() {
        Language language = Language.findLanguageByID("Markdown");
        if (language == null) {
            throw new IllegalStateException("Concordion md extension must not be registered without Markdown language");
        }
        return language;
    }

    @NotNull
    @Override
    public Set<String> fileExtensions() {
        return ImmutableSet.of("md", "markdown");
    }

    @Nullable
    @Override
    public String prefix(@NotNull PsiFile spec) {
        return "c";
    }

    @Nullable
    @Override
    public String extensionPrefix(@NotNull PsiFile spec) {
        PsiClass fixture = ConcordionNavigationService.getInstance(spec.getProject()).correspondingTestFixture(spec);
        return fixture != null ? extensionNamespace(fixture) : null;
    }

    @NotNull
    @Override
    public ConcordionSurrounder surrounder() {
        return ConcordionMdSurrounder.INSTANCE;
    }

    @NotNull
    @Override
    public ConcordionVariableUsageSearcher variablesUsageSearcher() {
        return ConcordionVariableInMdUsageSearcher.INSTANCE;
    }
}
