package org.concordion.plugin.idea.variables;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.variables.ConcordionVariableUsage.INVALID;

public class ConcordionVariableInHtmlUsageSearcher extends ConcordionVariableUsageSearcher {

    @Override
    protected int findEndOfScopePosition(@NotNull PsiElement injection) {
        //xmlAttributeValue -> xmlAttribute -> xmlTag -> closingTag
        PsiLanguageInjectionHost injectionHost = InjectedLanguageUtil.findInjectionHost(injection);
        if (injectionHost == null) {
            return -1;
        }
        XmlTag openingTag = PsiTreeUtil.getParentOfType(injectionHost, XmlTag.class);
        if (openingTag == null) {
            return -1;
        }
        PsiElement closingTag = openingTag.getLastChild();

        return closingTag != null
                ? closingTag.getTextOffset()
                : openingTag.getTextOffset();
    }

    @NotNull
    @Override
    protected ConcordionVariableUsage createUsage(@NotNull UsageInfo info) {
        if (!(info.owner.getParent() instanceof XmlAttributeValue)) {
            return INVALID;
        }
        XmlAttributeValue attributeValue = (XmlAttributeValue) info.owner.getParent();
        if (!(attributeValue.getParent() instanceof XmlAttribute)) {
            return INVALID;
        }
        XmlAttribute attribute = (XmlAttribute) attributeValue.getParent();
        PsiElement injected = InjectedLanguageUtil.findElementInInjected((PsiLanguageInjectionHost) attributeValue, info.position);
        if (injected == null || !(injected.getParent() instanceof ConcordionVariable)) {
            return INVALID;
        }
        return new ConcordionVariableUsage(attribute.getLocalName(), (ConcordionVariable) injected.getParent());
    }
}
