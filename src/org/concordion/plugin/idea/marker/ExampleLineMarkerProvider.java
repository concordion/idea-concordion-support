package org.concordion.plugin.idea.marker;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionBundle;
import org.concordion.plugin.idea.lang.ConcordionFile;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionField;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.concordion.plugin.idea.lang.psi.ConcordionStatement;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static org.concordion.plugin.idea.lang.psi.ConcordionTypes.EXAMPLE_NAME;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class ExampleLineMarkerProvider implements LineMarkerProvider {

    @NotNull
    public static final String EXAMPLE_PREFIX = ConcordionBundle.message("concordion.example.prefix") + " ";

    @NotNull
    private static final ConcordionElementPattern.Capture<?> EXAMPLE_COMMAND =
            concordionElement().with(new PatternCondition<PsiElement>("exampleCommand") {
        @Override
        public boolean accepts(@NotNull PsiElement element, ProcessingContext processingContext) {
            return element.getFirstChild() != null && element.getFirstChild().getNode().getElementType().equals(EXAMPLE_NAME);
        }
    });

    @NotNull
    private static final ConcordionElementPattern.Capture<?> EXAMPLE_FIELD =
            concordionElement().withChild(
                    concordionElement(ConcordionOgnlExpressionStart.class).withChild(concordionElement(ConcordionField.class).with(new PatternCondition<ConcordionField>("exampleField") {
                        @Override
                        public boolean accepts(@NotNull ConcordionField field, ProcessingContext processingContext) {
                            return field.isExampleName();
                        }
                    }))
            );

    @NotNull
    private static final ConcordionElementPattern.Capture<?> PATTERN =
            concordionElement(ConcordionFile.class).withChild(concordionElement(ConcordionStatement.class).andOr(EXAMPLE_COMMAND, EXAMPLE_FIELD));

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        ProcessingContext context = new ProcessingContext();
        if (PATTERN.accepts(element, context)) {
            return exampleInfoFor(element);
        }
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }

    @NotNull
    private LineMarkerInfo<PsiElement> exampleInfoFor(@NotNull PsiElement element) {
        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                ConcordionIcons.EXAMPLE,
                Pass.UPDATE_ALL,
                ExampleLineMarkerProvider::generateTooltipForExample,
                null,
                GutterIconRenderer.Alignment.CENTER
        );
    }

    @NotNull
    private static String generateTooltipForExample(@NotNull PsiElement element) {
        return EXAMPLE_PREFIX + element.getText();
    }
}
