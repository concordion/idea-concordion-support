package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.Namespaces;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.concordion.plugin.idea.specifications.ConcordionSpecification;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionCommand.commands;
import static org.concordion.plugin.idea.ConcordionContextKeys.CONCORDION_EXTENSIONS;

public class ConcordionCommandsCompletionProvider extends CompletionProvider<CompletionParameters> implements ConcordionSettingsListener {

    @NotNull private final Namespaces namespace;
    @NotNull private final ConcordionSpecification specType;
    @NotNull private final LookupElementFactory lookupElementFactory;
    @NotNull private ConcordionCommandsCaseType caseType = ConcordionCommandsCaseType.BOTH;

    public ConcordionCommandsCompletionProvider(@NotNull Namespaces namespace, @NotNull ConcordionSpecification specType, @NotNull LookupElementFactory lookupElementFactory) {
        this.namespace = namespace;
        this.specType = specType;
        this.lookupElementFactory = lookupElementFactory;
        registerListener();
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

        result.addAllElements(completions(
                context, namespace, caseType, specType, lookupElementFactory.from(namespace, context)
        ));
    }

    @Override
    public void settingsChanged(@NotNull ConcordionSettings newState) {
        caseType = newState.getCommandsCaseType();
    }

    @NotNull
    private static Iterable<LookupElement> completions(
            @NotNull ProcessingContext context,
            @NotNull Namespaces namespace,
            @NotNull ConcordionCommandsCaseType caseType,
            @NotNull ConcordionSpecification specType,
            @NotNull Function<String, LookupElement> lookupElementCreator
    ) {

        return commands()
                        .filter(command -> command.fitsForCaseType(caseType))
                        .filter(command -> command.fitsSpecType(specType))
                        .filter(commandsForNamespace(namespace, context))
                        .map(command -> command.prefixedText(namespace.computePrefix(context)))
                        .map(lookupElementCreator)
                        .collect(toList());
    }

    @NotNull
    private static Predicate<ConcordionCommand> commandsForNamespace(@NotNull Namespaces namespace, @NotNull ProcessingContext context) {
        switch (namespace) {
            case CONCORDION:
                return ConcordionCommand::buildIn;
            case CONCORDION_EXTENSIONS:
                return command -> !command.buildIn() && command.enabledByExtensions(context.get(CONCORDION_EXTENSIONS));
            default:
                throw new IllegalStateException("Unreachable");
        }
    }

    @FunctionalInterface
    public interface LookupElementFactory {

        @NotNull
        Function<String, LookupElement> from(@NotNull Namespaces namespace, @NotNull ProcessingContext context);
    }
}