package com.gman.idea.plugin.concordion.inspection;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static com.gman.idea.plugin.concordion.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class UsingMapKeyAsFieldTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    /**
     * Does not resolve jdk types and qualified names tests
     */
    public void ignoreTestWarnUsingFieldAsKeyInMap() {
        copyJavaRunnerToConcordionProject("UsingMapKeyAsField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("UsingMapKeyAsField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.enableInspections(UsingMapKeyAsField.class);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(WARNING).withText("key").withDescription("Using map key as a field"), 2);
    }

    public void testDoNotWarnUsingNestedFields() {
        copyJavaRunnerToConcordionProject("UsingMapKeyAsField.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("UsingMapKeyAsField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.enableInspections(UsingMapKeyAsField.class);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("inner").withDescription("Using map key as a field"));
    }
}