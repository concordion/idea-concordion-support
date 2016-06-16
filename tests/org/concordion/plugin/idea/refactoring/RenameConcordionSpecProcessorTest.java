package org.concordion.plugin.idea.refactoring;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class RenameConcordionSpecProcessorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/navigation";
    }

    public void testRenameConcordionPairFromSpec() {

        copyTestFixtureToConcordionProject("Spec.java");
        copySpecToConcordionProject("Spec.html");

        myFixture.renameElement(findFileInProject("/resources/com/test/Spec.html"), "NewSpec.html");

        assertThat(findFileInProject("/src/com/test/Spec.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/Spec.html")).isNull();

        assertThat(findFileInProject("/src/com/test/NewSpec.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NewSpec.html")).isNotNull();
    }

    public void testRenameNonConcordionPairFromHtml() {

        copyTestFixtureToConcordionProject("NoNamespace.java");
        copySpecToConcordionProject("NoNamespace.html");

        myFixture.renameElement(findFileInProject("/resources/com/test/NoNamespace.html"), "NewNoNamespace.html");

        assertThat(findFileInProject("/src/com/test/NoNamespace.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NoNamespace.html")).isNull();

        assertThat(findFileInProject("/src/com/test/NewNoNamespace.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/NewNoNamespace.html")).isNotNull();
    }
}