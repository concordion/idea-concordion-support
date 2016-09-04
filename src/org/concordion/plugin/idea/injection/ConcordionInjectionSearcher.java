package org.concordion.plugin.idea.injection;

import com.intellij.openapi.util.*;
import org.concordion.plugin.idea.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

public final class ConcordionInjectionSearcher {

    private static final char SINGLE_QUOTE = '\'';
    private static final char DOUBLE_QUOTE = '\"';

    private ConcordionInjectionSearcher() {
    }

    @NotNull
    public static List<TextRange> findInjectionsIn(@NotNull String text) {
        List<TextRange> injections = new ArrayList<>();

        findInjectionUsingQuote(text, DOUBLE_QUOTE).collect(toCollection(() -> injections));
        findInjectionUsingQuote(text, SINGLE_QUOTE).collect(toCollection(() -> injections));

        return injections;
    }

    @NotNull
    private static Stream<TextRange> findInjectionUsingQuote(@NotNull String text, char quote) {
        return new TextReverseSearcher(text, "- " + quote).stream()
                .map(position -> createRange(text, position, quote))
                .filter(Objects::nonNull);
    }

    @Nullable
    private static TextRange createRange(@NotNull String text, int position, char quote) {
        int start = position + 3;
        int end = text.indexOf(quote, start);

        if (end == -1) {
            return null;
        }

        return new TextRange(start, end);
    }
}
