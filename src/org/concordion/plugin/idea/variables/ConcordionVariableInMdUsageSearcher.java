package org.concordion.plugin.idea.variables;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.concordion.plugin.idea.injection.ConcordionInjection;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.variables.ConcordionVariableUsage.INVALID;

public class ConcordionVariableInMdUsageSearcher extends ConcordionVariableUsageSearcher {

    @Override
    protected int findEndOfScopePosition(@NotNull PsiElement injection) {
        PsiLanguageInjectionHost injectionHost = InjectedLanguageUtil.findInjectionHost(injection);
        if (injectionHost == null) {
            return -1;
        }
        return injectionHost.getTextOffset();
    }

    @NotNull
    @Override
    protected ConcordionVariableUsage createUsage(@NotNull OwnerAndPosition ownerAndPosition) {
        return INVALID;
    }
}
