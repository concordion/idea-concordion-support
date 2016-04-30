package org.concordion.plugin.idea.inspection;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static com.intellij.lang.annotation.HighlightSeverity.WARNING;
import static org.concordion.plugin.idea.HighlightingAssert.*;

//TODO unstable because of "java.lang.AssertionError: Cannot restore"
public class UsingUndeclaredVariableInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        myFixture.enableInspections(UsingUndeclaredVariable.class);

        copyTestFixtureToConcordionProject("ResolvingVariables.java");
        VirtualFile mdSpec = copySpecToConcordionProject("ResolvingVariables.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
    }

    public void testWarnUndeclaredVariableUsage() {

        assertThat(myFixture.doHighlighting())
                .has(usingUndeclaredVariable().withText("#undefined"));
    }

    public void ignoredTestWarnUsingBeforeDeclaring() {

        //May go forward for references table in the end of the file and find #definedToLate
        assertThat(myFixture.doHighlighting())
                .has(usingUndeclaredVariable().withText("#definedToLate"));
    }

    public void testDoNotWarnUsageOfDeclaredVariable() {

        assertThat(myFixture.doHighlighting())
                .hasNo(usingUndeclaredVariable().withText("#definedBeforehand"));
    }

    public void testDoNotWarnUsageOfReservedVariableWithoutDeclaration() {

        assertThat(myFixture.doHighlighting())
                .hasNo(usingUndeclaredVariable().withText("#TEXT"));
    }

    private Info usingUndeclaredVariable() {
        return anInfo().withSeverity(WARNING).withText("#TEXT").withDescription("Using undeclared variable");
    }
}