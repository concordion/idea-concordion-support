package org.concordion.plugin.idea.inspection;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static com.intellij.lang.annotation.HighlightSeverity.WARNING;
import static org.concordion.plugin.idea.HighlightingAssert.*;
import static org.concordion.plugin.idea.settings.ConcordionCommandsCaseType.*;

public class WrongCommandCaseUsedInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/inspection";
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        myFixture.enableInspections(WrongCommandCaseUsed.class);

        copyTestFixtureToConcordionProject("WrongCaseCommand.java");
        VirtualFile mdSpec = copySpecToConcordionProject("WrongCaseCommand.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);
    }

    public void testWarnUsingSpinalCaseIfCamelCaseConfigured() {

        useCommandsCase(CAMEL_CASE);

        assertThat(myFixture.doHighlighting())
                .has(wrongCommandCaseUsed().withText("c:assert-equals"));
    }

    public void testWarnUsingCamelCaseIfSpinalCaseConfigured() {

        useCommandsCase(SPINAL_CASE);

        assertThat(myFixture.doHighlighting())
                .has(wrongCommandCaseUsed().withText("c:assertEquals"));
    }

    public void testNotWarnsIfBothCasesConfigured() {

        useCommandsCase(BOTH);

        assertThat(myFixture.doHighlighting())
                .hasNo(wrongCommandCaseUsed());
    }

    private Info wrongCommandCaseUsed() {
        return anInfo().withSeverity(WARNING).withDescription("Command is used in wrong case");
    }
}