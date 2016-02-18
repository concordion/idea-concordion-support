package org.concordion.plugin.idea;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import org.assertj.core.api.AbstractAssert;

import java.util.List;

public class HighlightingAssert extends AbstractAssert<HighlightingAssert, List<HighlightInfo>> {

    public static HighlightingAssert assertThat(List<HighlightInfo> actual) {
        return new HighlightingAssert(actual);
    }

    public HighlightingAssert(List<HighlightInfo> actual) {
        super(actual, HighlightingAssert.class);
    }

    public HighlightingAssert hasInjectedFragment(String fragment) {

        return has(anInfo().withSeverity(INJECTED).withText(fragment));
    }

    public HighlightingAssert hasInjectedFragments(String fragment, long count) {

        return has(anInfo().withSeverity(INJECTED).withText(fragment), count);
    }

    public HighlightingAssert hasNoInjectedFragments() {

        return hasNo(anInfo().withSeverity(INJECTED));
    }

    public HighlightingAssert has(Info info, long expectedCount) {

        long actualCount = count(info);
        if (actualCount != expectedCount) {
            failWithMessage("Expected %d fragment(s) '%s' with severity '%s' and description '%s' but found %d",
                    expectedCount, info.text, info.severity, info.description, actualCount);
        }

        return this;
    }

    public HighlightingAssert has(Info info) {
        return has(info, 1);
    }

    public HighlightingAssert hasNo(Info info) {
        return has(info, 0);
    }

    private long count(Info info) {
        return actual.stream()
                .filter(info::match)
                .count();
    }

    public static final String INJECTED = "INJECTED_FRAGMENT";

    public static Info anInfo() {
        return new Info();
    }

    public static class Info {
        private String severity;
        private String text;
        private String description;

        public Info withSeverity(String severity) {
            this.severity = severity;
            return this;
        }

        public Info withSeverity(HighlightSeverity severity) {
            return withSeverity(severity.getName());
        }

        public Info withText(String text) {
            this.text = text;
            return this;
        }

        public Info withDescription(String description) {
            this.description = description;
            return this;
        }

        public boolean match(HighlightInfo info) {
            return (severity == null || severity.equals(info.getSeverity().getName()))
                    && (text == null || text.equals(info.getText()))
                    && (description == null || description.equals(info.getDescription()));
        }

        public Info copy() {
            return new Info()
                    .withSeverity(severity)
                    .withText(text)
                    .withDescription(description);
        }
    }
}
