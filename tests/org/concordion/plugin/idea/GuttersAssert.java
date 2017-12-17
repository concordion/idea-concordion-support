package org.concordion.plugin.idea;

import com.intellij.codeInsight.daemon.GutterMark;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
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

        Assertions.assertThat(actual)
                .extracting(GutterMark::getTooltipText)
                .describedAs("Expects 1 concordion gutter")
                .hasSize(1);

        return this;
    }

    public GuttersAssert hasNoConcordionGutter() {

        Assertions.assertThat(actual)
                .extracting(GutterMark::getTooltipText)
                .describedAs("Expects no concordion gutter")
                .isEmpty();

        return this;
    }

    public GuttersAssert hasConcordionExampleGutter(@NotNull String exampleName) {
        String expectedToolTipText = "Example: " + exampleName;

        Assertions.assertThat(actual)
                .extracting(GutterMark::getTooltipText)
                .contains(expectedToolTipText);

        return this;
    }
}
