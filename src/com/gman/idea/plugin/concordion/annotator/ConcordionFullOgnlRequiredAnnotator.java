package com.gman.idea.plugin.concordion.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import org.concordion.internal.SimpleEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

public class ConcordionFullOgnlRequiredAnnotator implements Annotator {

    private static final Set<String> SET_COMMANDS = singleton("set");
    private static final Set<String> VERIFY_ROW_COMMANDS = new HashSet<>(asList("verifyRows", "verify-rows"));
    private static final Set<String> EVALUATE_COMMANDS = new HashSet<>(asList("assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "echo",
            "execute"));
    public static final String MESSAGE = "This may be valid OGNL expression, however it is considered complex by Concordion and will not be evaluated at runtime. Simplify it or use @FullOGNL annotation over test fixture";

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!isConcordionHtmlSpec(element.getContainingFile())
                || !(element instanceof XmlAttribute)
                || !isConcordionNamespace(((XmlAttribute) element).getNamespace())) {
            return;
        }

        XmlAttribute attribute = (XmlAttribute) element;
        XmlAttributeValue value = PsiTreeUtil.findChildOfType(attribute, XmlAttributeValue.class);

        if (value == null) {
            return;
        }

        PsiFile htmlSpec = element.getContainingFile();
        PsiClass javaRunner = correspondingJavaRunner(htmlSpec);

        if (javaRunner != null && isUsingFullOgnl(javaRunner)) {
            return;
        }

        if (SET_COMMANDS.contains(attribute.getLocalName())) {
            annotateIfHasErrors(
                    SimpleEvaluator::validateSetVariableExpression,
                    attribute.getValue(),
                    value,
                    holder
            );
        } else if (VERIFY_ROW_COMMANDS.contains(attribute.getLocalName())) {
            annotateIfHasErrors(
                    SimpleEvaluator::validateEvaluationExpression,
                    extractEvaluationExpressionFromVerifyRows(attribute.getValue()),
                    value,
                    holder
            );
        } else if (EVALUATE_COMMANDS.contains(attribute.getLocalName())) {
            annotateIfHasErrors(
                    SimpleEvaluator::validateEvaluationExpression,
                    attribute.getValue(),
                    value,
                    holder
            );
        }
    }

    private void annotateIfHasErrors(Consumer<String> validator, String value, XmlAttributeValue attributeValue, AnnotationHolder holder) {
        try {
            validator.accept(value);
        } catch (RuntimeException e) {
            if (e.getMessage().contains(value)) {
                holder.createErrorAnnotation(attributeValue, MESSAGE);
            }
        }
    }

    private String extractEvaluationExpressionFromVerifyRows(String evaluateRowsExpression) {
        int delimiterPosition = evaluateRowsExpression.indexOf(':');

        if (delimiterPosition == -1) {
            return "";
        }

        return evaluateRowsExpression.substring(delimiterPosition + 1).trim();
    }
}
