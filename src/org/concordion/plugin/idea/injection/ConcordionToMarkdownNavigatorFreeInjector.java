package org.concordion.plugin.idea.injection;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.concordion.plugin.idea.specifications.ConcordionMdSpecification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.concordion.plugin.idea.ConcordionPsiUtils.firstNotNull;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class ConcordionToMarkdownNavigatorFreeInjector implements MultiHostInjector {

    private static final ConcordionElementPattern.Capture<PsiElement> LINKS_TITLES_TO_INJECT = concordionElement(PsiElement.class)
            .withConfiguredSpecOfType(ConcordionMdSpecification.INSTANCE)
            .with(new PatternCondition<PsiElement>("markdownNavigatorFreeInjector") {
                @Override
                public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                    String nodeType = element.getNode().getElementType().toString();
                    return "EXPLICIT_LINK".equals(nodeType)
                            || "REFERENCE".equals(nodeType)
                            || nodeType.endsWith("_EXPLICIT_LINK");
                }
            })
            .withFoundTestFixture();

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement host) {

        if (!LINKS_TITLES_TO_INJECT.accepts(host)) {
            return;
        }

        String text = host.getText();
        if (text.length() < 4) {
            return;
        }

        TextRange range = firstNotNull(
                () -> findInjectionPlace(text, '"'),
                () -> findInjectionPlace(text, '\'')
        );

        if (range == null) {
            return;
        }

        registrar
                .startInjecting(ConcordionLanguage.INSTANCE)
                .addPlace(null, null, new ConcordionInjection(host), range)
                .doneInjecting();
    }

    @Nullable
    private TextRange findInjectionPlace(String text, char quote) {
        int injectionStart = text.indexOf("- " + quote);
        int injectionEnd = text.indexOf(quote, injectionStart + 3);

        return injectionStart != -1 && injectionEnd != -1
                ? new TextRange(injectionStart + 3, injectionEnd)
                : null;
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return ImmutableList.of(PsiElement.class);
    }
}
