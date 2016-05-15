package org.concordion.plugin.idea.specifications;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.Namespaces;
import org.concordion.plugin.idea.surround.ConcordionHtmlSurrounder;
import org.concordion.plugin.idea.surround.ConcordionSurrounder;
import org.concordion.plugin.idea.variables.ConcordionVariableInHtmlUsageSearcher;
import org.concordion.plugin.idea.variables.ConcordionVariableUsageSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ConcordionHtmlSpecification implements ConcordionSpecification {

    @NotNull
    public static final ConcordionSpecification INSTANCE = new ConcordionHtmlSpecification();

    @NotNull
    @Override
    public Set<String> fileExtensions() {
        return ImmutableSet.of("html", "xhtml");
    }

    @Nullable
    @Override
    public String prefix(@NotNull PsiFile spec) {
        return Namespaces.CONCORDION.prefixInFile(spec);
    }

    @Nullable
    @Override
    public String extensionPrefix(@NotNull PsiFile spec) {
        return Namespaces.CONCORDION_EXTENSIONS.prefixInFile(spec);
    }

    @NotNull
    @Override
    public ConcordionSurrounder surrounder() {
        return ConcordionHtmlSurrounder.INSTANCE;
    }

    @NotNull
    @Override
    public ConcordionVariableUsageSearcher variablesUsageSearcher() {
        return ConcordionVariableInHtmlUsageSearcher.INSTANCE;
    }
}
