package com.gman.idea.plugin.concordion.autocomplete;

import com.gman.idea.plugin.concordion.Concordion;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConcordionTestCommandsCompletionContributor extends CompletionContributor {

    public ConcordionTestCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(XmlToken.class).withParent(XmlAttributeValue.class).with(ConcordionCommand.INSTANCE).with(ConcordionHtmlSpec.INSTANCE),
                new CompositeProvider()
        );
    }

    private static final class CompositeProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            PsiFile htmlSpec = parameters.getOriginalFile();
            PsiClass psiClass = correspondingJavaRunner(htmlSpec);

            result.addAllElements(optionsForClass(psiClass));
        }

        private Iterable<LookupElement> optionsForClass(PsiClass psiClass) {
            //TODO exclude object methods, use different paters for different commands
            List<LookupElement> options = new ArrayList<>();
            options.addAll(stream(psiClass.getAllMethods()).map(this::name).map(LookupElementBuilder::create).collect(toList()));
            options.addAll(stream(psiClass.getAllFields()).map(this::name).map(LookupElementBuilder::create).collect(toList()));
            return options;
        }

        private String name(PsiNamedElement element) {
            return element.getName();
        }
    }
}
