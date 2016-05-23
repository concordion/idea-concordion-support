package org.concordion.plugin.idea.annotator;

import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static org.concordion.plugin.idea.HighlightingAssert.*;

public class UnexpectedDictionaryValueTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testDoesNotErrorOutProperUsage() {

        copyTestFixtureToConcordionProject("CorrectDictionaryUsage.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CorrectDictionaryUsage.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .hasNo(unexpectedDictionaryValue());
    }

    public void testErrorOutWrongDictionaryValueUsed() {

        copyTestFixtureToConcordionProject("WrongDictionaryValueUsage.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("WrongDictionaryValueUsage.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(unexpectedDictionaryValue().withText("ExpectedToFail"));
    }

    public void testErrorOutUsingDictionaryValuesWithExpressions() {

        copyTestFixtureToConcordionProject("MisplacedDictionaryValue.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("MisplacedDictionaryValue.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(myFixture.doHighlighting())
                .has(unexpectedDictionaryValue().withText("BestMatch"));
    }

    private static Info unexpectedDictionaryValue() {
        return anInfo().withSeverity(ERROR).withDescription("This value can not be used here");
    }
}