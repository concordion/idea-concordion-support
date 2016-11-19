package org.concordion.plugin.idea.injection;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.specifications.ConcordionMdSpecification;
import org.intellij.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.Optional.ofNullable;
import static org.concordion.plugin.idea.ConcordionCommand.EXAMPLE;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;
import static org.intellij.plugins.markdown.lang.MarkdownElementTypes.*;

public class ConcordionToMarkdownSupportInjector implements MultiHostInjector {

    private static final ConcordionElementPattern.Capture<PsiElement> LINKS_TITLES_TO_INJECT = concordionElement(PsiElement.class)
            .withConfiguredSpecOfType(ConcordionMdSpecification.INSTANCE)
            .withElementType(LINK_TITLE)
            .with(new PatternCondition<PsiElement>("notRegularLink") {
                @Override
                public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                    return destinationForLinkTitle(element).equals("-")
                            && isNotExample(element.getText());
                }
            })
            .withMinTextLength(2)
            .withFoundTestFixture();

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement host) {

        if (!LINKS_TITLES_TO_INJECT.accepts(host)) {
            return;
        }

        registrar
                .startInjecting(ConcordionLanguage.INSTANCE)
                .addPlace(null, null, new ConcordionInjection(host), new TextRange(1, host.getTextLength() - 1))
                .doneInjecting();
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return ImmutableList.of(PsiElement.class);
    }

    @NotNull
    private static String destinationForLinkTitle(@NotNull PsiElement linkTitle) {
        return ofNullable(linkTitle.getParent())
                .map(PsiElement::getNode)
                .map(n -> n.findChildByType(MarkdownElementTypes.LINK_DESTINATION))
                .map(ASTNode::getText)
                .orElse("");
    }

    private static final int COMMAND_OFFSET = "'c:".length();

    private static boolean isNotExample(@NotNull String text) {
        return text.length() < COMMAND_OFFSET
                || !text.substring(COMMAND_OFFSET).startsWith(EXAMPLE.text());
    }
}
