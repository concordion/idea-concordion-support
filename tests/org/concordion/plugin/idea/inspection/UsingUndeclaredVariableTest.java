package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

import static org.concordion.plugin.idea.HighlightingAssert.*;
import static com.intellij.lang.annotation.HighlightSeverity.*;

public class UsingUndeclaredVariableTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        myFixture.enableInspections(UsingUndeclaredVariable.class);

        copyTestFixtureToConcordionProject("ResolvingVariables.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ResolvingVariables.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
    }

    public void testWarnUndeclaredVariableUsage() {

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(WARNING).withText("#undefined").withDescription("Using undeclared variable"));
    }

    public void testWarnUsingBeforeDeclaring() {

        assertThat(myFixture.doHighlighting())
                .has(anInfo().withSeverity(WARNING).withText("#definedToLate").withDescription("Using undeclared variable"));
    }

    public void testDoNotWarnUsageOfDeclaredVariable() {

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("#definedBeforehand").withDescription("Using undeclared variable"));
    }

    public void testDoNotWarnUsageOfNestedDeclaration() {

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("#definedInside").withDescription("Using undeclared variable"));
    }

    public void testDoNotWarnUsageOfReservedFariableWithoutDeclaration() {

        assertThat(myFixture.doHighlighting())
                .hasNo(anInfo().withSeverity(WARNING).withText("#TEXT").withDescription("Using undeclared variable"));
    }

    //TODO warn overwriting reserved variables
}