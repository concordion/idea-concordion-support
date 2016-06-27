package org.concordion.plugin.idea.variables;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.ConcordionPsiUtils;
import org.concordion.plugin.idea.lang.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.stream.Collectors.toSet;
import static org.concordion.plugin.idea.ConcordionCommand.*;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.iterableParameterType;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public class ConcordionVariableUsage {

    public static final ConcordionVariableUsage INVALID = new ConcordionVariableUsage(null, null);

    private static final Set<String> COMMANDS_THAT_CAN_SET_VARIABLE = Stream.of(SET, EXECUTE, VERIFY_ROWS_CAMEL, VERIFY_ROWS_SPINAL).map(ConcordionCommand::text).collect(toSet());

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
                && variableParent.getParent() instanceof ConcordionStatement
                && SET.text().equals(command)) {
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
            return iterableParameterType(iterator, expr.getProject());
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
