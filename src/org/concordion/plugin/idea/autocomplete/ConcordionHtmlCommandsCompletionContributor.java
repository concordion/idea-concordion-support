package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.Namespaces;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static org.concordion.plugin.idea.Namespaces.*;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionSpecType.*;

public class ConcordionHtmlCommandsCompletionContributor extends CompletionContributor {

    public ConcordionHtmlCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).andOr(
                        concordionElement().withConfiguredSpecOfType(HTML),
                        concordionElement().withFoundTestFixture()
                ),
                new ConcordionCommandsCompletionProvider(CONCORDION, HTML, new HtmlLookupElementFactory())
        );
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).withFoundTestFixture().withConfiguredExtensions(true),
                new ConcordionCommandsCompletionProvider(CONCORDION_EXTENSIONS, HTML, new HtmlLookupElementFactory())
        );
    }

    private static final class HtmlLookupElementFactory implements ConcordionCommandsCompletionProvider.LookupElementFactory {
        @NotNull
        @Override
        public Function<String, LookupElement> from(@NotNull Namespaces namespace, @NotNull ProcessingContext context) {
            ConcordionCommandInsertionHandler handler = namespace.prefixPrecomputed(context)
                    ? ConcordionCommandInsertionHandler.INSTANCE
                    : new ConcordionCommandInsertionHandler(namespace);

            return command -> LookupElementBuilder.create(command).withInsertHandler(handler).withIcon(ConcordionIcons.ICON);
        }
    }
}
