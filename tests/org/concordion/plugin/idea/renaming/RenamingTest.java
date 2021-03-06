package org.concordion.plugin.idea.renaming;

import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

public class RenamingTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/renaming";
    }

    public void testRenamePropertyInRunnerAfterRenamingInSpec() {
        copyTestFixtureToConcordionProject("RenamePropertyFromSpec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("RenamePropertyFromSpec.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenamePropertyFromSpec.java", "after/RenamePropertyFromSpec.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenamePropertyFromSpec.html", "after/RenamePropertyFromSpec.html", false);
    }

    public void testRenamePropertyInSpecAfterRenamingInRunner() {
        VirtualFile javaRunner = copyTestFixtureToConcordionProject("RenamePropertyFromRunner.java");
        copySpecToConcordionProject("RenamePropertyFromRunner.html");

        myFixture.configureFromExistingVirtualFile(javaRunner);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenamePropertyFromRunner.java", "after/RenamePropertyFromRunner.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenamePropertyFromRunner.html", "after/RenamePropertyFromRunner.html", false);
    }

    public void testRenameMethodInRunnerAfterRenamingInSpec() {
        copyTestFixtureToConcordionProject("RenameMethodFromSpec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("RenameMethodFromSpec.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenameMethodFromSpec.java", "after/RenameMethodFromSpec.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenameMethodFromSpec.html", "after/RenameMethodFromSpec.html", false);
    }

    public void testRenameMethodInSpecAfterRenamingInRunner() {
        VirtualFile javaRunner = copyTestFixtureToConcordionProject("RenameMethodFromRunner.java");
        copySpecToConcordionProject("RenameMethodFromRunner.html");

        myFixture.configureFromExistingVirtualFile(javaRunner);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenameMethodFromRunner.java", "after/RenameMethodFromRunner.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenameMethodFromRunner.html", "after/RenameMethodFromRunner.html", false);
    }

    public void testRenameVariableInSpec() {
        copyTestFixtureToConcordionProject("RenameVariable.java");
        VirtualFile spec = copySpecToConcordionProject("RenameVariable.html");

        myFixture.configureFromExistingVirtualFile(spec);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenameVariable.java", "after/RenameVariable.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenameVariable.html", "after/RenameVariable.html", false);
    }
}
