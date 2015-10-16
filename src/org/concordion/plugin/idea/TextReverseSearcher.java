package org.concordion.plugin.idea;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TextReverseSearcher implements Iterable<Integer> {

    @NotNull private final String text;
    @NotNull private final String search;
    private final int searchLength;
    private final int endOfScope;

    public TextReverseSearcher(@NotNull String text, @NotNull String search) {
        this(text, search, text.length());
    }

    public TextReverseSearcher(@NotNull String text, @NotNull String search, int endOfScope) {
        this.text = text;
        this.search = search;
        this.searchLength = search.length();
        this.endOfScope = endOfScope;
    }

    /**
     * Searching bottom to top, this is useful to find last declaration of variable
     */
    private int nextSearchPosition(int currentPosition) {
        return text.lastIndexOf(search, currentPosition - searchLength);
    }

    public Stream<Integer> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            private int nextPosition = nextSearchPosition(endOfScope + searchLength);

            @Override
            public boolean hasNext() {
                return nextPosition >= 0;
            }

            @Override
            public Integer next() {
                int currentPosition = nextPosition;
                nextPosition = nextSearchPosition(currentPosition);
                return currentPosition;
            }
        };
    }
}
