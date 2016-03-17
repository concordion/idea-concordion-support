package org.concordion.plugin.idea.variables;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.concordion.plugin.idea.injection.ConcordionInjection;
import org.concordion.plugin.idea.lang.psi.ConcordionEmbeddedCommand;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.*;
import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
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
    protected ConcordionVariableUsage createUsage(@NotNull UsageInfo info) {
        PsiElement owner = findHost(info.owner);

        PsiElement injected = findElementInHostWithManyInjections(new ConcordionInjection(owner), info.position);
        if (injected == null || !(injected.getParent() instanceof ConcordionVariable)) {
            return INVALID;
        }
        return new ConcordionVariableUsage(embeddedCommandIn(injected), (ConcordionVariable) injected.getParent());
    }

    @Nullable
    private String embeddedCommandIn(@NotNull PsiElement injected) {
        PsiFile file = getParentOfType(injected, PsiFile.class);
        ConcordionEmbeddedCommand command = findChildOfType(file, ConcordionEmbeddedCommand.class);
        return commandText(command);
    }

    @NotNull
    private PsiElement findHost(@NotNull PsiElement owner) {
        if ("Markdown:Markdown:TEXT".equals(owner.getNode().getElementType().toString())) {
            return owner.getParent();
        }
        return owner;
    }
}
