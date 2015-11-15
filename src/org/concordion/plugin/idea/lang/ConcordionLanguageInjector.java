package org.concordion.plugin.idea.lang;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.concordion.plugin.idea.ConcordionElementPattern;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static java.util.Collections.singletonList;

public class ConcordionLanguageInjector implements MultiHostInjector {

    private static final Set<String> CONCORDION_TAGS_FOR_EXPRESSION_INJECTION = ImmutableSet.of(
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "echo",
            "execute",
            "set",
            "verifyRows", "verify-rows"
    );

    private static final ConcordionElementPattern.Capture<XmlAttributeValue> TAGS_TO_INJECT = concordionElement(XmlAttributeValue.class)
            .withConcordionHtmlSpec()
            .withConcordionSchemaAttribute()
            .withConcordionCommand(CONCORDION_TAGS_FOR_EXPRESSION_INJECTION)
            .withFoundTestFixture();

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {

        if (TAGS_TO_INJECT.accepts(context)) {
            PsiLanguageInjectionHost value = (PsiLanguageInjectionHost) context;

            registrar
                    .startInjecting(ConcordionLanguage.INSTANCE)
                    .addPlace(null, null, value, new TextRange(1, value.getTextLength() - 1))
                    .doneInjecting();
        }

    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return singletonList(XmlAttributeValue.class);
    }
}
