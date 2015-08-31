package com.gman.idea.plugin.concordion.autocomplete;

import com.gman.idea.plugin.concordion.Concordion;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;

public class ConcordionCommandsCompletionContributor extends CompletionContributor {

    public ConcordionCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().withParent(XmlAttribute.class).with(ConcordionHtmlSpec.INSTANCE),
                new ConcordionCommandCompletionProvider()
        );
    }

    private static final class ConcordionCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            String schemaPrefix = concordionSchemaPrefixOf(parameters.getOriginalFile());

            for (String command : Concordion.COMMANDS) {
                result.addElement(createLookupElement(schemaPrefix, command));
            }
        }

        private LookupElement createLookupElement(String schemaPrefix, String command) {
            return LookupElementBuilder.create(schemaPrefix + ':' + command).withInsertHandler(XmlAttributeInsertHandler.INSTANCE);
        }
    }
}
