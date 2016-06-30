package org.concordion.plugin.idea.refactoring;

import com.intellij.refactoring.rename.RenameProcessor;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.concordion.plugin.idea.ConcordionPsiUtils.classIn;

public class RenameConcordionSpecProcessorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/navigation";
    }

    public void testRenameConcordionPairFromSpec() {

        copyTestFixtureToConcordionProject("Spec.java");
        copySpecToConcordionProject("Spec.html");

        new RenameProcessor(getProject(), findFileInProject("/resources/com/test/Spec.html"), "NewSpec.html", false, false).run();

        assertThat(findFileInProject("/src/com/test/Spec.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/Spec.html")).isNull();

        assertThat(findFileInProject("/src/com/test/NewSpec.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NewSpec.html")).isNotNull();
    }

    public void testRenameNonConcordionPairFromHtml() {

        copyTestFixtureToConcordionProject("NoNamespace.java");
        copySpecToConcordionProject("NoNamespace.html");

        new RenameProcessor(getProject(), findFileInProject("/resources/com/test/NoNamespace.html"), "NewNoNamespace.html", false, false).run();

        assertThat(findFileInProject("/src/com/test/NoNamespace.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NoNamespace.html")).isNull();

        assertThat(findFileInProject("/src/com/test/NewNoNamespace.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/NewNoNamespace.html")).isNotNull();
    }
}