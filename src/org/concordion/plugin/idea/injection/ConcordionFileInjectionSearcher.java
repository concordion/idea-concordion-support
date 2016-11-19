package org.concordion.plugin.idea.injection;

import com.intellij.openapi.util.*;
import org.concordion.plugin.idea.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.ConcordionCommand.EXAMPLE;

public final class ConcordionFileInjectionSearcher {

    private static final char SINGLE_QUOTE = '\'';
    private static final char DOUBLE_QUOTE = '\"';

    private ConcordionFileInjectionSearcher() {
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
                .filter(position -> isNotExample(text, position))
                .map(position -> createRange(text, position, quote))
                .filter(Objects::nonNull);
    }

    private static final int COMMAND_OFFSET = "- 'c:".length();

    private static boolean isNotExample(@NotNull String text, int position) {
        return text.length() < COMMAND_OFFSET
                || !text.substring(position + COMMAND_OFFSET).startsWith(EXAMPLE.text());
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
