package org.concordion.plugin.idea.autocomplete;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionElementPattern;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static java.util.stream.Collectors.toList;

public class ConcordionCommandsCompletionContributor extends CompletionContributor {

    private static final Collection<String> DEFAULT_COMMANDS = ImmutableList.of(
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "execute",
            "set",
            "echo",
            "verifyRows", "verify-rows",
            "matchStrategy", "match-strategy",
            "matchingRole", "matching-role",
            "run",
            "example",
            "status"
    );

    public static final Map<String, String> EXTENSION_COMMANDS = ImmutableMap.of(
            "org.concordion.ext.EmbedExtension", "embed",
            "org.concordion.ext.ExecuteOnlyIfExtension", "executeOnlyIf",
            "org.concordion.ext.ScreenshotExtension", "screenshot");

    public ConcordionCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).withConcordionHtmlSpec(),
                new ConcordionCommandCompletionProvider()
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).withConcordionHtmlSpec().withFoundTestFixture().withConfiguredExtensions(),
                new ConcordionExtensionCommandCompletionProvider()
        );
    }

    private static final class ConcordionCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    context.get(ConcordionElementPattern.CONCORDION_SCHEMA_PREFIX),
                    DEFAULT_COMMANDS
            ));
        }
    }

    private static final class ConcordionExtensionCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    context.get(ConcordionElementPattern.CONCORDION_EXTENSIONS_SCHEMA_PREFIX),
                    context.get(ConcordionElementPattern.CONCORDION_EXTENSIONS).stream()
                            .map(EXTENSION_COMMANDS::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList())
            ));
        }
    }

    private static Iterable<LookupElement> forCommands(String prefix, Collection<String> commands) {
        return commands.stream()
                .map(c -> prefix + ':' + c)
                .map(c -> LookupElementBuilder.create(c).withInsertHandler(ConcordionCommandInsertionHandler.INSTANCE))
                .collect(toList());
    }
}
