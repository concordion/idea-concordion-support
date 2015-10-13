package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class UsingMapKeyAsFieldTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(UsingMapKeyAsField.class);
    }

    /**
     * Does not resolve jdk types and qualified names in tests
     */
    public void ignoredTestWarnUsingFieldAsKeyInMap() {
        copyJavaRunnerToConcordionProject("UsingMapKeyAsField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("UsingMapKeyAsField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(WARNING).withText("key").withDescription("Using map key as a field"), 2);
    }

    public void testDoNotWarnUsingNestedFields() {
        copyJavaRunnerToConcordionProject("UsingMapKeyAsField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("UsingMapKeyAsField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("inner").withDescription("Using map key as a field"));
    }
}