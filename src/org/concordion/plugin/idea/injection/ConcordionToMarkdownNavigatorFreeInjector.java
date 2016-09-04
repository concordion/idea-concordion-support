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

import java.util.List;

import static org.concordion.plugin.idea.injection.ConcordionFileInjectionSearcher.findInjectionsIn;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class ConcordionToMarkdownNavigatorFreeInjector implements MultiHostInjector {

    private static final ConcordionElementPattern.Capture<PsiElement> LINKS_TITLES_TO_INJECT = concordionElement(PsiElement.class)
            .withConfiguredSpecOfType(ConcordionMdSpecification.INSTANCE)
            .with(new PatternCondition<PsiElement>("markdownNavigatorFreeInjector") {
                @Override
                public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                    String nodeType = element.getNode().getElementType().toString();
                    return "LINK_REF".equals(nodeType)
                            || "REFERENCE".equals(nodeType)
                            || "EXPLICIT_LINK".equals(nodeType)
                            || nodeType.endsWith("_EXPLICIT_LINK");
                }
            })
            .withFoundTestFixture();

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement host) {

        if (!LINKS_TITLES_TO_INJECT.accepts(host)) {
            return;
        }

        List<TextRange> injections = findInjectionsIn(host.getText());

        if (injections.isEmpty()) {
            return;
        }

        ConcordionInjection injectionHost = new ConcordionInjection(host);

        for (TextRange injection : injections) {
            registrar.startInjecting(ConcordionLanguage.INSTANCE);
            registrar.addPlace(null, null, injectionHost, injection);
            registrar.doneInjecting();
        }
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return ImmutableList.of(PsiElement.class);
    }
}
