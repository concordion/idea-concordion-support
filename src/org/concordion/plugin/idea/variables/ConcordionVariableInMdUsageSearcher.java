package org.concordion.plugin.idea.variables;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.concordion.plugin.idea.injection.ConcordionInjection;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionCommands.*;
import static org.concordion.plugin.idea.variables.ConcordionVariableUsage.INVALID;

public class ConcordionVariableInMdUsageSearcher extends ConcordionVariableUsageSearcher {

    @Override
    protected int findEndOfScopePosition(@NotNull PsiElement injection) {
        PsiLanguageInjectionHost injectionHost = InjectedLanguageUtil.findInjectionHost(injection);
        if (injectionHost == null) {
            return -1;
        }
        return injectionHost.getTextRange().getEndOffset();
    }

    @NotNull
    @Override
    protected ConcordionVariableUsage createUsage(@NotNull OwnerAndPosition ownerAndPosition) {
        PsiElement owner = findHost(ownerAndPosition.owner);

        PsiElement injected = InjectedLanguageUtil.findElementInInjected(new ConcordionInjection(owner), ownerAndPosition.position);
        if (injected == null || !(injected.getParent() instanceof ConcordionVariable)) {
            return INVALID;
        }
        return new ConcordionVariableUsage(extractCommand(owner.getText()), (ConcordionVariable) injected.getParent());
    }

    @NotNull
    private String extractCommand(@NotNull String text) {
        String command = findCommandInMdInjection(text);
        return command != null ? removePrefixIfPresent(command) : "set";
    }

    @NotNull
    private PsiElement findHost(@NotNull PsiElement owner) {
        if ("Markdown:Markdown:TEXT".equals(owner.getNode().getElementType().toString())) {
            return owner.getParent();
        }
        return owner;
    }
}
