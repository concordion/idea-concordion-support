package org.concordion.plugin.idea.annotator;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static org.concordion.plugin.idea.HighlightingAssert.*;

public class UnresolvedFieldAnnotatorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testErrorOutUnresolvedFields() {

        copyTestFixtureToConcordionProject("ResolvingFields.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ResolvingFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(ERROR).withText("unresolvedProperty").withDescription("Field unresolvedProperty is not found in class ResolvingFields"));
    }

    public void testDoesNotErrorOutResolvedFields() {

        copyTestFixtureToConcordionProject("ResolvingFields.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ResolvingFields.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("resolvedProperty"));
    }

    public void testDoesNotErrorOutArraysLengthProperty() {

        copyTestFixtureToConcordionProject("LengthOfArray.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("LengthOfArray.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(ERROR).withText("length"));
    }
}