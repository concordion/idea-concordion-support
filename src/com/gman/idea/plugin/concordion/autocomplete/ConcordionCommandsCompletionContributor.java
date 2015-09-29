package com.gman.idea.plugin.concordion.autocomplete;

import com.gman.idea.plugin.concordion.Concordion;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionPatternConditions.CONCORDION_HTML_SPEC;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static java.util.stream.Collectors.toList;

public class ConcordionCommandsCompletionContributor extends CompletionContributor {

    public ConcordionCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                psiElement().withParent(XmlAttribute.class).with(CONCORDION_HTML_SPEC),
                new ConcordionCommandCompletionProvider()
        );
    }

    private static final class ConcordionCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    concordionSchemaPrefixOf(parameters.getOriginalFile()),
                    Concordion.COMMANDS
            ));
        }
    }

    private static Iterable<LookupElement> forCommands(String prefix, Collection<String> commands) {
        return commands.stream()
                .map(c -> prefix + ':' + c)
                .map(c -> LookupElementBuilder.create(c).withInsertHandler(XmlAttributeInsertHandler.INSTANCE))
                .collect(toList());
    }
}
