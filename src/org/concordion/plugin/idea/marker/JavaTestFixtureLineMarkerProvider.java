package org.concordion.plugin.idea.marker;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;

import static org.concordion.plugin.idea.ConcordionContextKeys.TEST_FIXTURE_IDENTIFIER;
import static org.concordion.plugin.idea.patterns.ConcordionElementPattern.PARENT_OF_THE_PARENT;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class JavaTestFixtureLineMarkerProvider extends AbstractLineMarkerProvider {

    public JavaTestFixtureLineMarkerProvider() {
        super(
                concordionElement(PsiIdentifier.class)
                        .withParent(PsiClass.class)
                        .withSuperParent(PARENT_OF_THE_PARENT, PsiFile.class)
                        .withFoundSpecOfAnyType(),
                TEST_FIXTURE_IDENTIFIER
        );
    }
}
