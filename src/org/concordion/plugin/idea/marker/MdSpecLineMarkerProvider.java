package org.concordion.plugin.idea.marker;

import com.intellij.psi.PsiFile;

import static org.concordion.plugin.idea.ConcordionContextKeys.SPEC;
import static org.concordion.plugin.idea.ConcordionSpecType.MD;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class MdSpecLineMarkerProvider extends AbstractLineMarkerProvider {

    public MdSpecLineMarkerProvider() {
        super(
                concordionElement(PsiFile.class)
                        .withSpecOfType(MD)
                        .withFoundTestFixture(),
                SPEC
        );
    }
}
