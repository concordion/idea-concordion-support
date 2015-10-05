package com.gman.idea.plugin.concordion.lang;

import com.gman.idea.plugin.concordion.ConcordionElementPattern;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static com.gman.idea.plugin.concordion.ConcordionPatterns.concordionElement;
import static com.gman.idea.plugin.concordion.ConcordionPsiUtils.setOf;
import static java.util.Collections.singletonList;

public class ConcordionLanguageInjector implements MultiHostInjector {

    private static final Set<String> CONCORDION_TAGS_FOR_EXPRESSION_INJECTION = setOf(
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
