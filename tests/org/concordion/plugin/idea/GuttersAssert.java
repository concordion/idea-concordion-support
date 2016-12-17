package org.concordion.plugin.idea;

import com.intellij.codeInsight.daemon.GutterMark;
import org.assertj.core.api.AbstractAssert;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GuttersAssert extends AbstractAssert<GuttersAssert, Collection<GutterMark>> {

    public static GuttersAssert assertThat(Collection<GutterMark> actual) {
        return new GuttersAssert(actual);
    }

    public GuttersAssert(Collection<GutterMark> actual) {
        super(actual, GuttersAssert.class);
    }

    public GuttersAssert hasConcordionGutter() {
        long actualCount = countConcordionGutter();
        if (actualCount != 1) {
            failWithMessage("Expected 1 concordion gutter, but found %d", actualCount);
        }
        return this;
    }

    public GuttersAssert hasNoConcordionGutter() {
        long actualCount = countConcordionGutter();
        if (actualCount != 0) {
            failWithMessage("Expected no concordion gutters, but found %d", actualCount);
        }
        return this;
    }

    public GuttersAssert hasConcordionExampleGutter(@NotNull String exampleName) {
        String expectedToolTipText = "Example: " + exampleName;
        long foundExamples = actual.stream()
                .filter(g -> expectedToolTipText.equals(g.getTooltipText()))
                .count();
        if (foundExamples != 1) {
            failWithMessage("Example %s is not found", exampleName);
        }
        return this;
    }

    private long countConcordionGutter() {
        return actual.stream()
                .filter(g -> ConcordionLanguage.INSTANCE.getDisplayName().equals(g.getTooltipText()))
                .count();
    }
}
