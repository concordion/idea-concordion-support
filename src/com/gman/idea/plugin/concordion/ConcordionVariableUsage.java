package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.ConcordionInjectionUtils.getTopLevelFile;

public class ConcordionVariableUsage {

    @Nullable private XmlAttribute attribute;
    @Nullable private XmlAttributeValue attributeValue;
    @Nullable private ConcordionVariable variable;
    @Nullable private PsiElement variableParent;

    @Nullable
    public static ConcordionVariableUsage findDeclaration(@NotNull ConcordionVariableInternal variable) {
        PsiFile htmlSpec = getTopLevelFile(variable);
        if (htmlSpec == null) {
            return null;
        }
        String text = htmlSpec.getText();
        String varName = variable.getName();
        if (varName == null) {
            return null;
        }

        int endOfScopePosition = findEndOfScopePosition(variable);
        for (int pos = text.indexOf(varName);
             pos != -1 && pos <= endOfScopePosition;
             pos = text.indexOf(varName, pos+1)) {

            PsiElement elementAt = htmlSpec.findElementAt(pos);
            if (elementAt != null) {
                ConcordionVariableUsage usage = fromTokenAt(elementAt, pos);
                if (usage.isDeclaration()) {
                    return usage;
                }
            }
        }

        return null;
    }

    private static int findEndOfScopePosition(@NotNull ConcordionVariableInternal variable) {
        //xmlAttributeValue -> xmlAttribute -> xmlTag -> closingTag
        PsiLanguageInjectionHost injectionHost = InjectedLanguageUtil.findInjectionHost(variable);
        if (injectionHost == null
                || injectionHost.getParent() == null
                || injectionHost.getParent().getParent() == null
                || injectionHost.getParent().getParent().getFirstChild() == null) {
            return -1;
        }
        return injectionHost.getParent().getParent().getLastChild().getTextOffset();
    }

    @NotNull
    private static ConcordionVariableUsage fromTokenAt(@NotNull PsiElement element, int pos) {
        XmlAttribute attribute = null;
        XmlAttributeValue value = null;
        ConcordionVariable variable = null;
        PsiElement variableParen = null;

        if (element.getParent() instanceof XmlAttributeValue) {
            value = (XmlAttributeValue) element.getParent();
        }
        if (value != null && value.getParent() instanceof XmlAttribute) {
            attribute = (XmlAttribute) value.getParent();
        }
        if (value != null) {
            PsiElement injected = InjectedLanguageUtil.findElementInInjected((PsiLanguageInjectionHost) value, pos);
            if (injected != null && injected.getParent() instanceof ConcordionVariable) {
                variable = (ConcordionVariable) injected.getParent();
            }
        }
        if (variable != null) {
            variableParen = variable.getParent();
        }

        return new ConcordionVariableUsage(attribute, value, variable, variableParen);
    }

    private ConcordionVariableUsage(
            @Nullable XmlAttribute attribute,
            @Nullable XmlAttributeValue attributeValue,
            @Nullable ConcordionVariable variable,
            @Nullable PsiElement variableParent) {
        this.attribute = attribute;
        this.attributeValue = attributeValue;
        this.variable = variable;
        this.variableParent = variableParent;
    }

    public boolean isDeclaration() {
        if (attribute == null) {
            return false;
        }
        if (!"set".equals(attribute.getLocalName())
                && !"execute".equals(attribute.getLocalName())) {
            return false;
        }
        if (variableParent instanceof ConcordionConcordionSetExpression) {
            return true;
        }
        if (variableParent instanceof ConcordionOgnlExpressionStart
                && variableParent.getParent() instanceof ConcordionConcordionExpression
                && "set".equals(attribute.getLocalName())) {
            return true;
        }
        return false;
    }

    @NotNull
    public PsiType determineType() {
        return PsiType.NULL;
    }

    @Nullable
    public PsiElement resolve() {
        return variable;
    }
}
