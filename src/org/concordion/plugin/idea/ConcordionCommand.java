package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableList;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.concordion.plugin.idea.specifications.ConcordionMdSpecification;
import org.concordion.plugin.idea.specifications.ConcordionSpecification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class ConcordionCommand {

    private static final Collection<ConcordionCommand> ALL = new ArrayList<>();

    public static final ConcordionCommand ASSERT_EQUALS_CAMEL = camelCaseCommand("assertEquals").register();
    public static final ConcordionCommand ASSERT_EQUALS_SPINAL = spinalCaseCommand("assert-equals").register();

    public static final ConcordionCommand ASSERT_TRUE_CAMEL = camelCaseCommand("assertTrue").register();
    public static final ConcordionCommand ASSERT_TRUE_SPINAL = spinalCaseCommand("assert-true").register();

    public static final ConcordionCommand ASSERT_FALSE_CAMEL = camelCaseCommand("assertFalse").register();
    public static final ConcordionCommand ASSERT_FALSE_SPINAL = spinalCaseCommand("assert-false").register();

    public static final ConcordionCommand EXECUTE = command("execute").register();
    public static final ConcordionCommand SET = command("set").register();
    public static final ConcordionCommand ECHO = command("echo").register();
    public static final ConcordionCommand RUN = command("run").register();
    public static final ConcordionCommand EXAMPLE = command("example").register();
    public static final ConcordionCommand STATUS = command("status").withNonExpressionValues("ExpectedToPass", "ExpectedToFail", "Unimplemented").register();

    public static final ConcordionCommand VERIFY_ROWS_CAMEL = camelCaseCommand("verifyRows").register();
    public static final ConcordionCommand VERIFY_ROWS_SPINAL = spinalCaseCommand("verify-rows").register();

    public static final ConcordionCommand MATCH_STRATEGY_CAMEL = camelCaseCommand("matchStrategy").withNonExpressionValues("Default", "BestMatch", "KeyMatch").register();
    public static final ConcordionCommand MATCH_STRATEGY_SPINAL = spinalCaseCommand("match-strategy").withNonExpressionValues("Default", "BestMatch", "KeyMatch").register();

    public static final ConcordionCommand MATCHING_ROLE_CAMEL = camelCaseCommand("matchingRole").withNonExpressionValues("key").register();
    public static final ConcordionCommand MATCHING_ROLE_SPINAL = spinalCaseCommand("matching-role").withNonExpressionValues("key").register();

    public static final ConcordionCommand EMBED = command("embed").withExtension("org.concordion.ext.EmbedExtension").register();
    public static final ConcordionCommand EXECUTE_IF_ONLY = command("executeOnlyIf").withExtension("org.concordion.ext.ExecuteOnlyIfExtension").register();
    public static final ConcordionCommand SCREEN_SHOT = command("screenshot").withExtension("org.concordion.ext.ScreenshotExtension").withNonExpressionValues("linked").register();

    public static final ConcordionCommand ASSERT_EQUALS_MD = new ConcordionCommand("?", ConcordionCommandsCaseType.BOTH, ImmutableList.of(), null, ConcordionMdSpecification.INSTANCE) {
        @NotNull
        @Override
        public String prefixedText(@NotNull String prefix) {
            return text();
        }
    }.register();

    public static Stream<ConcordionCommand> commands() {
        return ALL.stream();
    }

    @NotNull
    private static ConcordionCommand camelCaseCommand(@NotNull String text) {
        return new ConcordionCommand(text, ConcordionCommandsCaseType.CAMEL_CASE);
    }

    @NotNull
    private static ConcordionCommand spinalCaseCommand(@NotNull String text) {
        return new ConcordionCommand(text, ConcordionCommandsCaseType.SPINAL_CASE);
    }

    @NotNull
    private static ConcordionCommand command(@NotNull String text) {
        return new ConcordionCommand(text, ConcordionCommandsCaseType.BOTH);
    }

    @NotNull private final String text;
    @NotNull private final ConcordionCommandsCaseType caseType;
    @NotNull private final List<String> nonExpressionValues;
    @Nullable private final String extension;
    @Nullable private final ConcordionSpecification specSpecific;

    private ConcordionCommand(@NotNull String text, @NotNull ConcordionCommandsCaseType caseType) {
        this(text, caseType, ImmutableList.of(), null, null);
    }

    private ConcordionCommand(@NotNull String text, @NotNull ConcordionCommandsCaseType caseType, @NotNull List<String> nonExpressionValues, @Nullable String extension, @Nullable ConcordionSpecification specSpecific) {
        this.text = text;
        this.caseType = caseType;
        this.nonExpressionValues = nonExpressionValues;
        this.extension = extension;
        this.specSpecific = specSpecific;
    }

    @NotNull
    public String text() {
        return text;
    }

    @NotNull
    public String prefixedText(@NotNull String prefix) {
        return prefix + ':' + text;
    }

    public boolean fitsForCaseType(@NotNull ConcordionCommandsCaseType caseType) {
        return caseType == ConcordionCommandsCaseType.BOTH
                || this.caseType == ConcordionCommandsCaseType.BOTH
                || this.caseType == caseType;
    }

    public boolean fitsSpecType(@NotNull ConcordionSpecification specType) {
        return specSpecific == null || specSpecific.equals(specType);
    }

    public boolean buildIn() {
        return extension == null;
    }

    public boolean extension() {
        return !buildIn();
    }

    public boolean enabledByExtensions(@NotNull Set<String> extensions) {
        return extensions.contains(extension);
    }

    public boolean expression() {
        return nonExpressionValues.isEmpty();
    }

    public boolean nonExpression() {
        return !expression();
    }

    @NotNull
    public List<String> nonExpressionValues() {
            return nonExpressionValues;
        }

    @NotNull
    private ConcordionCommand withNonExpressionValues(@NotNull String... values) {
        return new ConcordionCommand(text, caseType, ImmutableList.copyOf(values), extension, specSpecific);
    }

    @NotNull
    private ConcordionCommand withExtension(@Nullable String extension) {
        return new ConcordionCommand(text, caseType, nonExpressionValues, extension, specSpecific);
    }

    @NotNull
    protected final ConcordionCommand register() {
        ALL.add(this);
        return this;
    }
}
