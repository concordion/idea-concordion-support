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
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("RegularClass.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }

    public void testDoesNotErrorOutAnnotatedTestFixture() {

        copyHtmlSpecToConcordionProject("SimpleExpressions.html");
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("SimpleExpressions.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }

    public void testErrorOutNotAnnotatedTestFixture() {

        copyHtmlSpecToConcordionProject("RunnerNotAnnotated.html");
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("RunnerNotAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("RunnerNotAnnotated").withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }

    public void testErrorOutTestFixtureAnnotatedWithDifferentRunner() {

        copyHtmlSpecToConcordionProject("RunnerWrongAnnotated.html");
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("RunnerWrongAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("RunnerWrongAnnotated").withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));

    }

    public void testDoesNotErrorOutTestFixtureWithInheritedAnnotation() {

        copyHtmlSpecToConcordionProject("InheritedAnnotation.html");
        copyJavaRunnerToConcordionProject("AnnotatedParent.java");
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("InheritedAnnotation.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)"));
    }
}