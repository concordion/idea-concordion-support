package org.concordion.plugin.idea.injection;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.concordion.plugin.idea.ConcordionElementPattern;
import org.concordion.plugin.idea.TextReverseSearcher;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionSpecType.MD;

public class ConcordionToMarkdownInjector implements MultiHostInjector {

    private static final ConcordionElementPattern.Capture<PsiElement> FILES_TO_INJECT = concordionElement(PsiElement.class)
            .withConfiguredSpecOfType(MD)
            .withFoundTestFixture();

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement host) {

        if (!FILES_TO_INJECT.accepts(host)) {
            return;
        }

        List<TextRange> injections = new ArrayList<>();
        injections.addAll(findInjectionPlaces(host.getText(), '\''));
        injections.addAll(findInjectionPlaces(host.getText(), '"'));

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
    private List<TextRange> findInjectionPlaces(String text, char quote) {
        return new TextReverseSearcher(text, "- " + quote).stream()
                .map(position -> createRange(text, position, quote))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @Nullable
    private TextRange createRange(String text, int position, char quote) {
        int start = position + 3;
        int end = text.indexOf(quote, start);

        if (end == -1 || end <= start) {
            return null;
        }

        return new TextRange(start, end);
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return ImmutableList.of(PsiElement.class);
    }
}
