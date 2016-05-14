package org.concordion.plugin.idea.marker;

import com.intellij.psi.PsiFile;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.GrClassDefinition;

import static org.concordion.plugin.idea.ConcordionContextKeys.TEST_FIXTURE;
import static org.concordion.plugin.idea.patterns.ConcordionElementPattern.PARENT_OF_THE_PARENT;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class GroovyTestFixtureLineMarkerProvider extends AbstractLineMarkerProvider {

    public GroovyTestFixtureLineMarkerProvider() {
        super(
                concordionElement(GroovyTokenTypes.mIDENT)
                        .withParent(GrClassDefinition.class)
                        .withSuperParent(PARENT_OF_THE_PARENT, PsiFile.class)
                        .withFoundSpecOfAnyType(),
                TEST_FIXTURE
        );
    }
}
