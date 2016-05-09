package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.Namespaces;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static org.concordion.plugin.idea.Namespaces.*;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.*;
import static org.concordion.plugin.idea.ConcordionSpecType.MD;

public class ConcordionEmbeddedCommandsCompletionContributor extends CompletionContributor {

    public ConcordionEmbeddedCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withSpecOfType(MD).andOr(
                        concordionElement(ConcordionTypes.COMMAND),
                        concordionElement(ConcordionTypes.IDENTIFIER).withStartOfInjection()
                ),
                new ConcordionCommandsCompletionProvider(CONCORDION, MD, new MdLookupElementFactory())
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withSpecOfType(MD).withFoundTestFixture().withConfiguredExtensions(false).andOr(
                        concordionElement(ConcordionTypes.COMMAND),
                        concordionElement(ConcordionTypes.IDENTIFIER).withStartOfInjection()
                ),
                new ConcordionCommandsCompletionProvider(CONCORDION_EXTENSIONS, MD, new MdLookupElementFactory())
        );
    }

    private static final class MdLookupElementFactory implements ConcordionCommandsCompletionProvider.LookupElementFactory {
        @NotNull
        @Override
        public Function<String, LookupElement> from(@NotNull Namespaces namespace, @NotNull ProcessingContext context) {
            return command -> LookupElementBuilder.create(command + '=').withIcon(ConcordionIcons.ICON);
        }
    }
}
