package com.gman.idea.plugin.concordion.renaming;

import com.gman.idea.plugin.concordion.ConcordionCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.VirtualFile;

public class RenamingTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/renaming";
    }

    public void testShouldRenamePropertyInRunnerAfterRenamingInSpec() {
        copyJavaRunnerToConcordionProject("RenamePropertyFromSpec.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("RenamePropertyFromSpec.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenamePropertyFromSpec.java", "after/RenamePropertyFromSpec.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenamePropertyFromSpec.html", "after/RenamePropertyFromSpec.html", false);
    }

    public void testShouldRenamePropertyInSpecAfterRenamingInRunner() {
        VirtualFile javaRunner = copyJavaRunnerToConcordionProject("RenamePropertyFromRunner.java");
        copyHtmlSpecToConcordionProject("RenamePropertyFromRunner.html");

        myFixture.configureFromExistingVirtualFile(javaRunner);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenamePropertyFromRunner.java", "after/RenamePropertyFromRunner.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenamePropertyFromRunner.html", "after/RenamePropertyFromRunner.html", false);
    }

    public void testShouldRenameMethodInRunnerAfterRenamingInSpec() {
        copyJavaRunnerToConcordionProject("RenameMethodFromSpec.java");
        VirtualFile htmlSpec = copyHtmlSpecToConcordionProject("RenameMethodFromSpec.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenameMethodFromSpec.java", "after/RenameMethodFromSpec.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenameMethodFromSpec.html", "after/RenameMethodFromSpec.html", false);
    }

    public void testShouldRenameMethodInSpecAfterRenamingInRunner() {
        VirtualFile javaRunner = copyJavaRunnerToConcordionProject("RenameMethodFromRunner.java");
        copyHtmlSpecToConcordionProject("RenameMethodFromRunner.html");

        myFixture.configureFromExistingVirtualFile(javaRunner);
        myFixture.renameElementAtCaret("afterRename");

        myFixture.checkResultByFile("/src/com/test/RenameMethodFromRunner.java", "after/RenameMethodFromRunner.java", false);
        myFixture.checkResultByFile("/resources/com/test/RenameMethodFromRunner.html", "after/RenameMethodFromRunner.html", false);
    }

}
