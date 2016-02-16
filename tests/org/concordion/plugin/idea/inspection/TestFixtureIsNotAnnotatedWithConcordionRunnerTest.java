package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class TestFixtureIsNotAnnotatedWithConcordionRunnerTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(TestFixtureIsNotAnnotatedWithConcordionRunner.class);
    }

    public void testDoesNotErrorOutRegularClassWithoutHtmlSpec() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RegularClass.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }

    public void testDoesNotErrorOutAnnotatedTestFixture() {

        copySpecToConcordionProject("SimpleExpressions.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("SimpleExpressions.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }

    public void testErrorOutNotAnnotatedTestFixture() {

        copySpecToConcordionProject("RunnerNotAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerNotAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("RunnerNotAnnotated").withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }

    public void testErrorOutTestFixtureAnnotatedWithDifferentRunner() {

        copySpecToConcordionProject("RunnerWrongAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerWrongAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("RunnerWrongAnnotated").withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));

    }

    public void testDoesNotErrorOutTestFixtureWithInheritedAnnotation() {

        copySpecToConcordionProject("InheritedAnnotation.html");
        copyTestFixtureToConcordionProject("AnnotatedParent.java");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("InheritedAnnotation.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }
}