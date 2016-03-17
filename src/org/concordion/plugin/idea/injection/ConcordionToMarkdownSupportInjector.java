package org.concordion.plugin.idea.injection;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.concordion.plugin.idea.ConcordionElementPattern;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.intellij.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionSpecType.MD;

public class ConcordionToMarkdownSupportInjector implements MultiHostInjector {

    private static final ConcordionElementPattern.Capture<PsiElement> LINKS_TITLES_TO_INJECT = concordionElement(PsiElement.class)
            .withConfiguredSpecOfType(MD)
            .withElementType(MarkdownElementTypes.LINK_TITLE)
            .withFoundTestFixture();

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement host) {

        if (!LINKS_TITLES_TO_INJECT.accepts(host)) {
            return;
        }

        String text = host.getText();

        if (text.length() <= 2) {
            return;
        }

        registrar
                .startInjecting(ConcordionLanguage.INSTANCE)
                .addPlace(null, null, new ConcordionInjection(host), new TextRange(1, text.length() - 1))
                .doneInjecting();
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return ImmutableList.of(PsiElement.class);
    }
}
