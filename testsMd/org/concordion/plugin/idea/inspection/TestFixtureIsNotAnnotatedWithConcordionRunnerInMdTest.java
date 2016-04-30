package org.concordion.plugin.idea.inspection;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static org.concordion.plugin.idea.HighlightingAssert.*;

public class TestFixtureIsNotAnnotatedWithConcordionRunnerInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(TestFixtureIsNotAnnotatedWithConcordionRunner.class);
    }

    public void testDoesNotErrorOutAnnotatedTestFixture() {

        copySpecToConcordionProject("SimpleExpressions.md");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("SimpleExpressions.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(testFixtureIsNotAnnotatedWithConcordionRunner());
    }

    public void testErrorOutNotAnnotatedTestFixture() {

        copySpecToConcordionProject("RunnerNotAnnotated.md");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerNotAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(testFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerNotAnnotated"));
    }

    public void testErrorOutTestFixtureAnnotatedWithDifferentRunner() {

        copySpecToConcordionProject("RunnerWrongAnnotated.md");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerWrongAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(testFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerWrongAnnotated"));

    }

    public void testDoesNotErrorOutTestFixtureWithInheritedAnnotation() {

        copySpecToConcordionProject("InheritedAnnotation.md");
        copyTestFixtureToConcordionProject("AnnotatedParent.java");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("InheritedAnnotation.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(testFixtureIsNotAnnotatedWithConcordionRunner());
    }

    private Info testFixtureIsNotAnnotatedWithConcordionRunner() {
        return anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)");
    }
}