package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.ConcordionFile;
import com.gman.idea.plugin.concordion.lang.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.gman.idea.plugin.concordion.ConcordionInjectionUtils.getTopLevelFile;
import static com.gman.idea.plugin.concordion.ConcordionPsiUtils.*;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class ConcordionVariableUsage {

    private static final Set<String> COMMANDS_THAT_CAN_SET_VARIABLE = setOf("set", "execute", "verifyRows", "verify-rows");
    private static final Set<String> RESERVED_VARIABLES = setOf("TEXT", "HREF", "LEVEL");

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
        int varNameLength = varName.length();
        int endOfScopePosition = findEndOfScopePosition(variable);

        for (int pos = text.lastIndexOf(varName, endOfScopePosition); pos >= 0; pos = text.lastIndexOf(varName, pos - varNameLength)) {
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
    private static ConcordionVariableUsage fromTokenAt(@NotNull PsiElement element, int pos) {
        ConcordionVariableUsage usage = new ConcordionVariableUsage();

        if (element.getParent() instanceof XmlAttributeValue) {
            usage.attributeValue = (XmlAttributeValue) element.getParent();
        }
        if (usage.attributeValue != null && usage.attributeValue.getParent() instanceof XmlAttribute) {
            usage.attribute = (XmlAttribute) usage.attributeValue.getParent();
        }
        if (usage.attributeValue != null) {
            PsiElement injected = InjectedLanguageUtil.findElementInInjected((PsiLanguageInjectionHost) usage.attributeValue, pos);
            if (injected != null && injected.getParent() instanceof ConcordionVariable) {
                usage.variable = (ConcordionVariable) injected.getParent();
            }
        }
        if (usage.variable != null) {
            usage.variableParent = usage.variable.getParent();
        }

        return usage;
    }

    public boolean isDeclaration() {
        if (attribute == null) {
            return false;
        }
        if (variable != null && RESERVED_VARIABLES.contains(variable.getName())) {
            return true;
        }
        if (!COMMANDS_THAT_CAN_SET_VARIABLE.contains(attribute.getLocalName())) {
            return false;
        }
        if (variableParent instanceof ConcordionSetExpression) {
            return true;
        }
        if (variableParent instanceof ConcordionOgnlExpressionStart
                && variableParent.getParent() instanceof ConcordionFile
                && "set".equals(attribute.getLocalName())) {
            return true;
        }
        if (variableParent instanceof ConcordionIterateExpression) {
            return true;
        }
        return false;
    }

    @Nullable
    public PsiType determineType() {
        //PsiType.NULL means resolved, but can be dynamically typed to Integer/Double/String
        if (variable == null || variableParent == null) {
            return null;
        }
        if (RESERVED_VARIABLES.contains(variable.getName())) {
            return PsiType.NULL;
        }
        if (variableParent instanceof ConcordionSetExpression) {
            ConcordionOgnlExpressionStart expr = findChildOfType(variableParent, ConcordionOgnlExpressionStart.class);
            return expr != null ? typeOfExpression(expr) : null;
        }
        if (variableParent instanceof ConcordionOgnlExpressionStart) {
            return PsiType.NULL;
        }
        if (variableParent instanceof ConcordionIterateExpression) {
            ConcordionOgnlExpressionStart expr = findChildOfType(variableParent, ConcordionOgnlExpressionStart.class);
            if (expr == null) {
                return null;
            }
            PsiType iterator = typeOfExpression(expr);
            if (iterator == null) {
                return null;
            }
            return listParameterType(iterator);
        }
        return null;
    }

    @Nullable
    public PsiElement resolve() {
        return variable;
    }
}
