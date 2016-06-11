package org.concordion.plugin.idea.specifications;

import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.action.intention.surround.ConcordionSurrounder;
import org.concordion.plugin.idea.variables.ConcordionVariableUsageSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

import static org.concordion.plugin.idea.ConcordionExtension.*;

public final class ConcordionSpecifications {

    private ConcordionSpecifications() {
    }

    public static boolean specConfiguredInFile(@NotNull PsiFile file) {
        return prefixInFile(file) != null;
    }

    @Nullable
    public static String prefixInFile(@NotNull PsiFile file) {
        return specExtension(file).map(spec -> spec.prefix(file)).orElse(null);
    }

    @Nullable
    public static String extensionPrefixInFile(@NotNull PsiFile file) {
        return specExtension(file).map(spec -> spec.extensionPrefix(file)).orElse(null);
    }

    @Nullable
    public static ConcordionSurrounder surrounderFor(@NotNull PsiFile file) {
        return specExtension(file).map(ConcordionSpecification::surrounder).orElse(null);
    }

    @Nullable
    public static ConcordionVariableUsageSearcher variableUsageSearcherFor(@NotNull PsiFile file) {
        return specExtension(file).map(ConcordionSpecification::variablesUsageSearcher).orElse(null);
    }

    @NotNull
    public static Stream<ConcordionSpecification> specifications() {
        return extensions(ConcordionSpecification.EP_NAME);
    }

    @NotNull
    private static Optional<ConcordionSpecification> specExtension(@NotNull PsiFile file) {
        return extension(ConcordionSpecification.EP_NAME, file);
    }
}
