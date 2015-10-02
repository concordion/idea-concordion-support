package com.gman.idea.plugin.concordion.inspection;

import com.gman.idea.plugin.concordion.ConcordionElementPattern;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.concordion.internal.SimpleEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.gman.idea.plugin.concordion.ConcordionPatterns.concordionElement;
import static com.gman.idea.plugin.concordion.ConcordionPsiUtils.setOf;

public class FullOgnlRequired extends LocalInspectionTool {

    private static final Set<String> SET_COMMANDS = setOf("set");
    private static final Set<String> VERIFY_ROW_COMMANDS = setOf("verifyRows", "verify-rows");
    private static final Set<String> EVALUATE_COMMANDS = setOf("assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "echo",
            "execute");


    private static final ConcordionElementPattern<XmlAttributeValue> TOO_COMPLEX_CONCORDION_EXPRESSION =
            concordionElement(XmlAttributeValue.class)
                    .withParent(XmlAttribute.class)
                    .withConcordionHtmlSpec()
                    .withFoundTestFixture()
                    .withFullOgnl(false)
                    .withConcordionSchemaAttribute()
                    .andOr(
                            concordionElement(XmlAttributeValue.class)
                                    .withConcordionCommand(SET_COMMANDS)
                                    .with(new ComplicatedExpression(
                                            SimpleEvaluator::validateSetVariableExpression
                                    )),
                            concordionElement(XmlAttributeValue.class)
                                    .withConcordionCommand(EVALUATE_COMMANDS)
                                    .with(new ComplicatedExpression(
                                            SimpleEvaluator::validateEvaluationExpression
                                    )),
                            concordionElement(XmlAttributeValue.class)
                                    .withConcordionCommand(VERIFY_ROW_COMMANDS)
                                    .with(new ComplicatedExpression(
                                            SimpleEvaluator::validateEvaluationExpression,
                                            FullOgnlRequired::extractEvaluationExpressionFromVerifyRows
                                    ))
                    );

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);

                if (TOO_COMPLEX_CONCORDION_EXPRESSION.accepts(element)) {
                    holder.registerProblem(element, "Not using @FullOGNL");
                }
            }
        };
    }

    private static String extractEvaluationExpressionFromVerifyRows(String evaluateRowsExpression) {
        int delimiterPosition = evaluateRowsExpression.indexOf(':');

        if (delimiterPosition == -1) {
            return "";
        }

        return evaluateRowsExpression.substring(delimiterPosition + 1).trim();
    }

    public static final class ComplicatedExpression extends PatternCondition<XmlAttributeValue> {

        private final Consumer<String> validator;
        private final Function<String, String> transformer;

        public ComplicatedExpression(Consumer<String> validator) {
            this(validator, null);
        }

        public ComplicatedExpression(Consumer<String> validator, Function<String, String> transformer) {
            super("SimpleExpression");
            this.validator = validator;
            this.transformer = transformer;
        }

        @Override
        public boolean accepts(@NotNull XmlAttributeValue attributeValue, ProcessingContext context) {
            try {
                String expression = attributeValue.getValue();
                if (transformer != null) {
                    expression = transformer.apply(expression);
                }
                validator.accept(expression);
                return false;//simple
            } catch (RuntimeException e){
                return true;//complex
            }
        }
    }
}
