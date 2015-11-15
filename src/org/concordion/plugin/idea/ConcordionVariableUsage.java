package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
import org.concordion.plugin.idea.lang.ConcordionFile;
import org.concordion.plugin.idea.lang.psi.*;
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

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;
import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class ConcordionVariableUsage {

    private static final Set<String> COMMANDS_THAT_CAN_SET_VARIABLE = ImmutableSet.of("set", "execute", "verifyRows", "verify-rows");
    private static final Set<String> RESERVED_VARIABLES = ImmutableSet.of("TEXT", "HREF", "LEVEL");

    @Nullable private XmlAttribute attribute;
    @Nullable private XmlAttributeValue attributeValue;
    @Nullable private ConcordionVariable variable;
    @Nullable private PsiElement variableParent;

    @NotNull
    public static List<ConcordionVariableUsage> findAllDeclarationsFrom(@Nullable PsiFile injection) {
        if (injection == null) {
            return emptyList();
        }
        PsiFile htmlSpec = getTopLevelFile(injection);
        if (htmlSpec == null) {
            return emptyList();
        }

        return new TextReverseSearcher(htmlSpec.getText(), "#", findEndOfScopePosition(injection)).stream()
                .map(pos -> ownerAndPosition(htmlSpec, pos))
                .filter(Objects::nonNull)
                .map(ConcordionVariableUsage::fromOwnerAndPosition)
                .filter(ConcordionVariableUsage::isDeclaration)
                .collect(toList());
    }

    @Nullable
    public static ConcordionVariableUsage findDeclaration(@NotNull ConcordionVariableInternal variable) {
        PsiFile htmlSpec = getTopLevelFile(variable);
        if (htmlSpec == null) {
            return null;
        }
        String varName = variable.getName();
        if (varName == null) {
            return null;
        }

        return new TextReverseSearcher(htmlSpec.getText(), varName, findEndOfScopePosition(variable)).stream()
                .map(pos -> ownerAndPosition(htmlSpec, pos))
                .filter(Objects::nonNull)
                .map(ConcordionVariableUsage::fromOwnerAndPosition)
                .filter(usage -> usage.isUsageOf(varName))
                .filter(ConcordionVariableUsage::isDeclaration)
                .findFirst().orElse(null);
    }

    private static int findEndOfScopePosition(@NotNull PsiElement injection) {
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
    private static ConcordionVariableUsage fromOwnerAndPosition(@NotNull OwnerAndPosition ownerAndPosition) {
        ConcordionVariableUsage usage = new ConcordionVariableUsage();

        if (ownerAndPosition.owner.getParent() instanceof XmlAttributeValue) {
            usage.attributeValue = (XmlAttributeValue) ownerAndPosition.owner.getParent();
        }
        if (usage.attributeValue != null && usage.attributeValue.getParent() instanceof XmlAttribute) {
            usage.attribute = (XmlAttribute) usage.attributeValue.getParent();
        }
        if (usage.attributeValue != null) {
            PsiElement injected = InjectedLanguageUtil.findElementInInjected((PsiLanguageInjectionHost) usage.attributeValue, ownerAndPosition.position);
            if (injected != null && injected.getParent() instanceof ConcordionVariable) {
                usage.variable = (ConcordionVariable) injected.getParent();
            }
        }
        if (usage.variable != null) {
            usage.variableParent = usage.variable.getParent();
        }

        return usage;
    }

    @Nullable
    private static OwnerAndPosition ownerAndPosition(@NotNull PsiFile htmlSpec, int position) {
        PsiElement owner = htmlSpec.findElementAt(position);
        return owner != null ? new OwnerAndPosition(owner, position) : null;
    }

    private static final class OwnerAndPosition {
        @NotNull private final PsiElement owner;
        private final int position;

        public OwnerAndPosition(@NotNull PsiElement owner, int position) {
            this.owner = owner;
            this.position = position;
        }
    }

    public boolean isUsageOf(@NotNull String varName) {
        return variable != null && varName.equals(variable.getName());
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
            return ConcordionPsiUtils.DYNAMIC;
        }
        if (variableParent instanceof ConcordionSetExpression) {
            ConcordionOgnlExpressionStart expr = findChildOfType(variableParent, ConcordionOgnlExpressionStart.class);
            return expr != null ? typeOfExpression(expr) : null;
        }
        if (variableParent instanceof ConcordionOgnlExpressionStart) {
            return ConcordionPsiUtils.DYNAMIC;
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
            return iterableParameterType(iterator);
        }
        return null;
    }

    @Nullable
    public PsiElement resolve() {
        return variable;
    }

    @Nullable
    public String getName() {
        return variable != null ? '#' + nullToEmpty(variable.getName()) : null;
    }
}
