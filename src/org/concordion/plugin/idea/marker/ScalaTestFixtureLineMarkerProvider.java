package org.concordion.plugin.idea.marker;

import com.intellij.psi.PsiFile;
import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes;
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScClass;

import static org.concordion.plugin.idea.ConcordionContextKeys.TEST_FIXTURE_IDENTIFIER;
import static org.concordion.plugin.idea.patterns.ConcordionElementPattern.PARENT_OF_THE_PARENT_OF_THE_PARENT;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class ScalaTestFixtureLineMarkerProvider extends AbstractLineMarkerProvider {

    public ScalaTestFixtureLineMarkerProvider() {
        super(
                concordionElement(ScalaTokenTypes.tIDENTIFIER)
                        .withParent(ScClass.class)
                        .withSuperParent(PARENT_OF_THE_PARENT_OF_THE_PARENT, PsiFile.class)
                        .withFoundSpecOfAnyType(),
                TEST_FIXTURE_IDENTIFIER
        );
    }
}
