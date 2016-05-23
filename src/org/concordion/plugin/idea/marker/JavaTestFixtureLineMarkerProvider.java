package org.concordion.plugin.idea.marker;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;

import static org.concordion.plugin.idea.ConcordionContextKeys.*;
import static org.concordion.plugin.idea.patterns.ConcordionElementPattern.*;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.*;

public class JavaTestFixtureLineMarkerProvider extends AbstractLineMarkerProvider {

    public JavaTestFixtureLineMarkerProvider() {
        super(
                concordionElement(PsiIdentifier.class)
                        .withParent(PsiClass.class)
                        .withSuperParent(PARENT_OF_THE_PARENT, PsiFile.class)
                        .withFoundSpecOfAnyType(),
                TEST_FIXTURE
        );
    }
}
