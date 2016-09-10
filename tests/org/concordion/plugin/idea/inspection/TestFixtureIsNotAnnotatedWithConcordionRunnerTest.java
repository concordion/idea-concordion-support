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
        myFixture.enableInspections(GroovyTestFixtureIsNotAnnotatedWithConcordionRunner.class);
        myFixture.enableInspections(ScalaTestFixtureIsNotAnnotatedWithConcordionRunner.class);
    }

    public void testDoesNotErrorOutRegularClassWithoutHtmlSpec() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RegularClass.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(javaTestFixtureIsNotAnnotatedWithConcordionRunner());
    }

    public void testDoesNotErrorOutRegularGroovyClassWithoutHtmlSpec() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RegularClass.groovy");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(groovyTestFixtureIsNotAnnotatedWithConcordionRunner());
    }

    public void testDoesNotErrorOutRegularScalaClassWithoutHtmlSpec() {
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RegularClass.scala");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(scalaTestFixtureIsNotAnnotatedWithConcordionRunner());
    }

    public void testDoesNotErrorOutAnnotatedTestFixture() {

        copySpecToConcordionProject("SimpleExpressions.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("SimpleExpressions.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(javaTestFixtureIsNotAnnotatedWithConcordionRunner());
    }

    public void testDoesNotErrorOutAnnotatedGroovyTestFixture() {

        copySpecToConcordionProject("SimpleExpressions.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("SimpleExpressions.groovy");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(groovyTestFixtureIsNotAnnotatedWithConcordionRunner());
    }

    public void testDoesNotErrorOutAnnotatedScalaTestFixture() {

        copySpecToConcordionProject("SimpleExpressions.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("SimpleExpressions.scala");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(scalaTestFixtureIsNotAnnotatedWithConcordionRunner());
    }

    public void testErrorOutNotAnnotatedTestFixture() {

        copySpecToConcordionProject("RunnerNotAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerNotAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(javaTestFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerNotAnnotated"));
    }

    public void testErrorOutNotAnnotatedGroovyTestFixture() {

        copySpecToConcordionProject("RunnerNotAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerNotAnnotated.groovy");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(groovyTestFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerNotAnnotated"));
    }

    public void testErrorOutNotAnnotatedScalaTestFixture() {

        copySpecToConcordionProject("RunnerNotAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerNotAnnotated.scala");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(scalaTestFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerNotAnnotated"));
    }

    public void testErrorOutTestFixtureAnnotatedWithDifferentRunner() {

        copySpecToConcordionProject("RunnerWrongAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerWrongAnnotated.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(javaTestFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerWrongAnnotated"));

    }

    public void testErrorOutGroovyTestFixtureAnnotatedWithDifferentRunner() {

        copySpecToConcordionProject("RunnerWrongAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerWrongAnnotated.groovy");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(groovyTestFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerWrongAnnotated"));

    }

    public void testErrorOutScalaTestFixtureAnnotatedWithDifferentRunner() {

        copySpecToConcordionProject("RunnerWrongAnnotated.html");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("RunnerWrongAnnotated.scala");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .has(scalaTestFixtureIsNotAnnotatedWithConcordionRunner().withText("RunnerWrongAnnotated"));

    }

    public void testDoesNotErrorOutTestFixtureWithInheritedAnnotation() {

        copySpecToConcordionProject("InheritedAnnotation.html");
        copyTestFixtureToConcordionProject("AnnotatedParent.java");
        VirtualFile testFixture = copyTestFixtureToConcordionProject("InheritedAnnotation.java");

        myFixture.configureFromExistingVirtualFile(testFixture);

        assertThat(myFixture.doHighlighting())
                .hasNo(javaTestFixtureIsNotAnnotatedWithConcordionRunner());
    }

    private Info javaTestFixtureIsNotAnnotatedWithConcordionRunner() {
        return anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)");
    }

    private Info groovyTestFixtureIsNotAnnotatedWithConcordionRunner() {
        return anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(ConcordionRunner.class)");
    }

    private Info scalaTestFixtureIsNotAnnotatedWithConcordionRunner() {
        return anInfo().withSeverity(ERROR).withDescription("Test fixture is not annotated with @RunWith(classOf[ConcordionRunner])");
    }
}