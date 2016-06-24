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

        copyTestFixtureToConcordionProject("UsingMapKeyAsField.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("UsingMapKeyAsField.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
    }

    public void testWarnUsingFieldAsKeyInMap() {

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(WARNING).withText("mapKey").withDescription("Using map key as a field"), 2);
    }

    public void testDoNotWarnUsingNestedFields() {

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("inner").withDescription("Using map key as a field"));
    }
}