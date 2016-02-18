package org.concordion.plugin.idea.variables;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import org.concordion.plugin.idea.ConcordionPsiUtils;
import org.concordion.plugin.idea.lang.ConcordionFile;
import org.concordion.plugin.idea.lang.psi.ConcordionIterateExpression;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.concordion.plugin.idea.lang.psi.ConcordionSetExpression;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.iterableParameterType;
import static org.concordion.plugin.idea.ConcordionPsiUtils.nullToEmpty;
import static org.concordion.plugin.idea.ConcordionPsiUtils.typeOfExpression;

public class ConcordionVariableUsage {

    public static final ConcordionVariableUsage INVALID = new ConcordionVariableUsage(null, null);

    private static final Set<String> COMMANDS_THAT_CAN_SET_VARIABLE = ImmutableSet.of("set", "execute", "verifyRows", "verify-rows");
    private static final Set<String> RESERVED_VARIABLES = ImmutableSet.of("TEXT", "HREF", "LEVEL");

    @Nullable private final String command;
    @Nullable private final ConcordionVariable variable;
    @Nullable private final PsiElement variableParent;

    public ConcordionVariableUsage(
            @Nullable String command,
            @Nullable ConcordionVariable variable
    ) {
        this.command = command;
        this.variable = variable;
        this.variableParent = variable != null ? variable.getParent() : null;
    }

    public boolean isUsageOf(@NotNull String varName) {
        return variable != null && varName.equals(variable.getName());
    }

    public boolean isDeclaration() {
        if (command == null) {
            return false;
        }
        if (variable != null && RESERVED_VARIABLES.contains(variable.getName())) {
            return true;
        }
        if (!COMMANDS_THAT_CAN_SET_VARIABLE.contains(command)) {
            return false;
        }
        if (variableParent instanceof ConcordionSetExpression) {
            return true;
        }
        if (variableParent instanceof ConcordionOgnlExpressionStart
                && variableParent.getParent() instanceof ConcordionFile
                && "set".equals(command)) {
            return true;
        }
        if (variableParent instanceof ConcordionIterateExpression) {
            return true;
        }
        return false;
    }

    @Nullable
    public PsiType determineType() {
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
