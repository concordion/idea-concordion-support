package org.concordion.plugin.idea.injection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttributeValue;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionSpecType.HTML;

public class ConcordionToHtmlInjector implements MultiHostInjector {

    private static final Set<String> CONCORDION_TAGS_FOR_EXPRESSION_INJECTION = ImmutableSet.of(
            /* build in */
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "echo",
            "execute",
            "set",
            "verifyRows", "verify-rows",
            /* extensions */
            "embed",
            "executeOnlyIf"
    );

    private static final ConcordionElementPattern.Capture<XmlAttributeValue> TAGS_TO_INJECT = concordionElement(XmlAttributeValue.class)
            .withConfiguredSpecOfType(HTML)
            .withConcordionXmlAttribute()
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
        return ImmutableList.of(XmlAttributeValue.class);
    }
}
