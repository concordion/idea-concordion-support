package org.concordion.plugin.idea.variables;

import org.concordion.plugin.idea.TextReverseSearcher;
import org.concordion.plugin.idea.lang.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.variableUsageSearcherFor;

public abstract class ConcordionVariableUsageSearcher {

    @NotNull
    public static List<ConcordionVariableUsage> findAllDeclarationsFrom(@NotNull PsiFile injection) {

        return declarations(injection, "")
                .collect(toList());
    }

    @Nullable
    public static ConcordionVariableUsage findDeclaration(@NotNull ConcordionVariableInternal variable) {
        String varName = variable.getName();
        if (varName == null) {
            return null;
        }

        return declarations(variable, varName)
                .filter(usage -> usage.isUsageOf(varName))
                .findFirst().orElse(null);
    }

    protected abstract int findEndOfScopePosition(@NotNull PsiElement injection);

    @NotNull
    protected abstract ConcordionVariableUsage createUsage(@NotNull UsageInfo usageInfo);

    @NotNull
    private static Stream<ConcordionVariableUsage> declarations(@NotNull PsiElement injection, @NotNull String name) {
        PsiFile spec = getTopLevelFile(injection);
        if (spec == null) {
            return Stream.empty();
        }
        ConcordionVariableUsageSearcher searcher = variableUsageSearcherFor(spec);
        if (searcher == null) {
            return Stream.empty();
        }
        return new TextReverseSearcher(spec.getText(), "#" + name, searcher.findEndOfScopePosition(injection)).stream()
                .map(pos -> usageInfo(spec, pos))
                .filter(Objects::nonNull)
                .map(searcher::createUsage)
                .filter(ConcordionVariableUsage::isDeclaration);
    }

    @Nullable
    private static UsageInfo usageInfo(@NotNull PsiFile htmlSpec, int position) {
        PsiElement owner = htmlSpec.findElementAt(position);
        return owner != null ? new UsageInfo(owner, position) : null;
    }

    protected static final class UsageInfo {
        @NotNull protected final PsiElement owner;
        protected final int position;

        public UsageInfo(@NotNull PsiElement owner, int position) {
            this.owner = owner;
            this.position = position;
        }
    }
}
