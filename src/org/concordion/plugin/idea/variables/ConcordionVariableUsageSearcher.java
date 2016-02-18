package org.concordion.plugin.idea.variables;

import com.google.common.collect.ImmutableMap;
import org.concordion.plugin.idea.TextReverseSearcher;
import org.concordion.plugin.idea.lang.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;

public abstract class ConcordionVariableUsageSearcher {

    @NotNull
    public static List<ConcordionVariableUsage> findAllDeclarationsFrom(@Nullable PsiFile injection) {
        if (injection == null) {
            return emptyList();
        }
        PsiFile spec = getTopLevelFile(injection);
        if (spec == null) {
            return emptyList();
        }
        ConcordionVariableUsageSearcher searcher = searcherFor(spec);
        if (searcher == null) {
            return emptyList();
        }

        return new TextReverseSearcher(spec.getText(), "#", searcher.findEndOfScopePosition(injection)).stream()
                .map(pos -> ownerAndPosition(spec, pos))
                .filter(Objects::nonNull)
                .map(searcher::createUsage)
                .filter(ConcordionVariableUsage::isDeclaration)
                .collect(toList());
    }

    @Nullable
    public static ConcordionVariableUsage findDeclaration(@NotNull ConcordionVariableInternal variable) {
        PsiFile spec = getTopLevelFile(variable);
        if (spec == null) {
            return null;
        }
        String varName = variable.getName();
        if (varName == null) {
            return null;
        }
        ConcordionVariableUsageSearcher searcher = searcherFor(spec);
        if (searcher == null) {
            return null;
        }

        return new TextReverseSearcher(spec.getText(), varName, searcher.findEndOfScopePosition(variable)).stream()
                .map(pos -> ownerAndPosition(spec, pos))
                .filter(Objects::nonNull)
                .map(searcher::createUsage)
                .filter(usage -> usage.isUsageOf(varName))
                .filter(ConcordionVariableUsage::isDeclaration)
                .findFirst().orElse(null);
    }

    protected abstract int findEndOfScopePosition(@NotNull PsiElement injection);

    @NotNull
    protected abstract ConcordionVariableUsage createUsage(@NotNull OwnerAndPosition ownerAndPosition);

    @Nullable
    private static ConcordionVariableUsageSearcher searcherFor(@NotNull PsiFile spec) {
        return SEARCHERS.get(spec.getFileType().getDefaultExtension());
    }

    private static final Map<String, ConcordionVariableUsageSearcher> SEARCHERS = ImmutableMap.of(
            "html", new ConcordionVariableInHtmlUsageSearcher(),
            "md", new ConcordionVariableInMdUsageSearcher()
    );

    @Nullable
    private static OwnerAndPosition ownerAndPosition(@NotNull PsiFile htmlSpec, int position) {
        PsiElement owner = htmlSpec.findElementAt(position);
        return owner != null ? new OwnerAndPosition(owner, position) : null;
    }

    protected static final class OwnerAndPosition {
        @NotNull protected final PsiElement owner;
        protected final int position;

        public OwnerAndPosition(@NotNull PsiElement owner, int position) {
            this.owner = owner;
            this.position = position;
        }
    }
}
