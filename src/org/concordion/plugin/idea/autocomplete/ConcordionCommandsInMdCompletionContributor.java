package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.lang.ConcordionFile;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.intellij.plugins.markdown.lang.MarkdownLanguage;
import org.jetbrains.annotations.NotNull;


import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionCommands.*;
import static org.concordion.plugin.idea.ConcordionElementPattern.*;
import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;

public class ConcordionCommandsInMdCompletionContributor extends CompletionContributor {

    public ConcordionCommandsInMdCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement()
                        .withSuperParent(PARENT_OF_THE_PARENT, ConcordionOgnlExpressionStart.class)
                        .withSuperParent(PARENT_OF_THE_PARENT_OF_THE_PARENT, ConcordionFile.class)
                        .with(new PatternCondition<PsiElement>("mdInjectionPattern") {
                            @Override
                            public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                                PsiLanguageInjectionHost host = InjectedLanguageUtil.findInjectionHost(element);
                                return host != null
                                        && host.getLanguage().equals(MarkdownLanguage.INSTANCE)
                                        && findCommandInMdInjection(host.getText()) == null;
                            }
                        }),
                new ConcordionCommandsInMdProvider()
        );
    }

    private static final class ConcordionCommandsInMdProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            result.addAllElements(mdCommands());
        }
    }

    @NotNull
    private static Iterable<LookupElement> mdCommands() {
        return MD_COMMANDS.stream()
                .map(c -> LookupElementBuilder.create(c + '=').withIcon(ConcordionIcons.ICON))
                .collect(toList());
    }
}
