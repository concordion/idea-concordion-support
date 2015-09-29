package com.gman.idea.plugin.concordion;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.gman.idea.plugin.concordion.Concordion.isConcordionHtmlSpec;
import static java.util.Arrays.asList;

public class ConcordionPatternConditions {

    public static final ConcordionHtmlSpec CONCORDION_HTML_SPEC = new ConcordionHtmlSpec();
    public static final SpecificConcordionTag MATCH_STRATEGY_ATTRIBUTE = new SpecificConcordionTag("matchStrategy", "match-strategy");
    public static final SpecificConcordionTag MATCHING_ROLE_ATTRIBUTE = new SpecificConcordionTag("matchingRole", "matching-Role");

    private static final class ConcordionHtmlSpec extends PatternCondition<PsiElement> {

        public ConcordionHtmlSpec() {
            super(ConcordionHtmlSpec.class.getName());
        }

        @Override
        public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
            return isConcordionHtmlSpec(element.getContainingFile());
        }
    };

    private static final class SpecificConcordionTag extends PatternCondition<PsiElement> {

        private final Set<String> expectedTagNames = new HashSet<>();

        public SpecificConcordionTag(String... expectedTagNames) {
            super(SpecificConcordionTag.class.getName());
            this.expectedTagNames.addAll(asList(expectedTagNames));
        }

        @Override
        public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
            String localName = ((XmlAttribute) element.getParent().getParent()).getLocalName();
            return expectedTagNames.contains(localName);
        }
    }
}
