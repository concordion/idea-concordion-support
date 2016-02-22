package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.Namespaces;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static org.concordion.plugin.idea.ConcordionCommands.*;
import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionContextKeys.*;
import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionSpecType.*;

public class ConcordionCommandsCompletionContributor extends CompletionContributor {

    public ConcordionCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).andOr(
                        concordionElement().withConfiguredSpecOfType(HTML),
                        concordionElement().withFoundTestFixture()
                ),
                new ConcordionCommandCompletionProvider()
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).withFoundTestFixture().withConfiguredExtensions(),
                new ConcordionExtensionCommandCompletionProvider()
        );
    }

    private static final class ConcordionCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    Namespaces.CONCORDION,
                    context.get(CONCORDION_SCHEMA_PREFIX),
                    DEFAULT_COMMANDS
            ));
        }
    }

    private static final class ConcordionExtensionCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    Namespaces.CONCORDION_EXTENSIONS,
                    context.get(CONCORDION_EXTENSIONS_SCHEMA_PREFIX),
                    extensionCommands(context.get(CONCORDION_EXTENSIONS))
            ));
        }
    }

    private static Iterable<LookupElement> forCommands(Namespaces namespace, String precomputedPrefix, Collection<String> commands) {

        String prefix = precomputedPrefix != null
                ? precomputedPrefix
                : namespace.defaultPrefix;

        ConcordionCommandInsertionHandler handler = precomputedPrefix != null
                ? ConcordionCommandInsertionHandler.INSTANCE
                : new ConcordionCommandInsertionHandler(namespace);

        return commandsWithPrefix(commands, prefix).stream()
                .map(c -> LookupElementBuilder.create(c).withInsertHandler(handler).withIcon(ConcordionIcons.ICON))
                .collect(toList());
    }
}
