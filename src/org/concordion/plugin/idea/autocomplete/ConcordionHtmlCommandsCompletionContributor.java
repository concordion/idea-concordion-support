package org.concordion.plugin.idea.autocomplete;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.Namespaces;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.concordion.plugin.idea.settings.ConcordionSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

import static org.concordion.plugin.idea.ConcordionCommand.*;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionContextKeys.*;
import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionSpecType.*;

public class ConcordionHtmlCommandsCompletionContributor extends CompletionContributor {

    public ConcordionHtmlCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).andOr(
                        concordionElement().withConfiguredSpecOfType(HTML),
                        concordionElement().withFoundTestFixture()
                ),
                new ConcordionHtmlCommandsCompletionProvider()
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).withFoundTestFixture().withConfiguredExtensions(true),
                new ConcordionHtmlExtensionCommandsCompletionProvider()
        );
    }

    private static final class ConcordionHtmlCommandsCompletionProvider extends CompletionProvider<CompletionParameters> implements ConcordionSettingsListener {

        private List<ConcordionCommand> commands = ImmutableList.of();

        public ConcordionHtmlCommandsCompletionProvider() {
            registerListener();
        }

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    Namespaces.CONCORDION,
                    context.get(CONCORDION_SCHEMA_PREFIX),
                    commands.stream()
            ));
        }

        @Override
        public void settingsChanged(@NotNull ConcordionSettingsState newSettings) {
            commands = commands()
                    .filter(command -> command.fitsForCaseType(newSettings.getCommandsCaseType()))
                    .filter(command -> command.fitsSpecType(HTML))
                    .filter(ConcordionCommand::buildIn)
                    .collect(toList());
        }
    }

    private static final class ConcordionHtmlExtensionCommandsCompletionProvider extends CompletionProvider<CompletionParameters> implements ConcordionSettingsListener {

        private List<ConcordionCommand> commands = ImmutableList.of();

        public ConcordionHtmlExtensionCommandsCompletionProvider() {
            registerListener();
        }

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    Namespaces.CONCORDION_EXTENSIONS,
                    context.get(CONCORDION_EXTENSIONS_SCHEMA_PREFIX),
                    commands.stream().filter(command -> command.enabledByExtensions(context.get(CONCORDION_EXTENSIONS)))
            ));
        }

        @Override
        public void settingsChanged(@NotNull ConcordionSettingsState newSettings) {
            commands = commands()
                    .filter(command -> command.fitsForCaseType(newSettings.getCommandsCaseType()))
                    .filter(command -> command.fitsSpecType(HTML))
                    .filter(ConcordionCommand::extension)
                    .collect(toList());
        }
    }

    @NotNull
    private static Iterable<LookupElement> forCommands(
            @NotNull Namespaces namespace,
            @Nullable String precomputedPrefix,
            @NotNull Stream<ConcordionCommand> commands
    ) {

        String prefix = precomputedPrefix != null
                ? precomputedPrefix
                : namespace.defaultPrefix;

        ConcordionCommandInsertionHandler handler = precomputedPrefix != null
                ? ConcordionCommandInsertionHandler.INSTANCE
                : new ConcordionCommandInsertionHandler(namespace);

        return commands
                .map(command -> command.prefixedText(prefix))
                .map(command -> LookupElementBuilder.create(command).withInsertHandler(handler).withIcon(ConcordionIcons.ICON))
                .collect(toList());
    }
}
