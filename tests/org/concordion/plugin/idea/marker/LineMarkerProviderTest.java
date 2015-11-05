package org.concordion.plugin.idea.marker;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.GuttersAssert.assertThat;

public class LineMarkerProviderTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/marker";
    }

    public void testSecAndFixtureHaveSameName() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec1.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec1.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();
    }

    public void testFixtureMayHaveOptionalTestSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec2Test.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec2.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();
    }

    public void testSpecShouldNotHaveTestSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec3Test.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec3Test.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();
    }

    public void testFixtureMayHaveOptionalFixtureSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec4Fixture.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec4.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();
    }

    public void testSpecShouldNotHaveFixtureSuffix() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("Spec5Fixture.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("Spec5Fixture.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();
    }

    public void testNoMarkerIfNoConcordionPresent() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("NoConcordion.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoConcordion.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();
    }

    public void testNoConcordionRunnerAnnotation() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("NoRunnerAnnotation.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoRunnerAnnotation.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();
    }

    public void testNoConcordionNamespace() {
        VirtualFile testFixture = copyJavaRunnerToConcordionProject("NoNamespace.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("NoNamespace.html");

        myFixture.configureFromExistingVirtualFile(testFixture);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        assertThat(myFixture.findAllGutters()).hasConcordionGutter();
    }
}
