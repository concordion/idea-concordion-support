package org.concordion.plugin.idea;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class TextReverseSearcherTest extends TestCase {

    public void testNothingForEmptyString() {
        assertThat(new TextReverseSearcher("", "#var").iterator())
                .hasSize(0);
    }

    public void testFindSingleEntry() {
        assertThat(new TextReverseSearcher("#var", "#var").iterator())
                .hasSize(1).contains(0);
    }

    public void testFindSeveralEntries() {
        assertThat(new TextReverseSearcher("#var,#var,#var", "#var").iterator())
                .hasSize(3).contains(10, 5, 0);
    }

    public void testIgnoreValueOutOfScope() {
        assertThat(new TextReverseSearcher("#var,#var,#var", "#var", 9).iterator())
                .hasSize(2).contains(5, 0).doesNotContain(10);
    }
}