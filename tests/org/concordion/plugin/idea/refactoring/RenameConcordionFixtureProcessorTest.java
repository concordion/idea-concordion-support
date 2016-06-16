package org.concordion.plugin.idea.refactoring;

import com.intellij.psi.PsiClass;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static org.assertj.core.api.Assertions.assertThat;

public class RenameConcordionFixtureProcessorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/navigation";
    }

    public void testRenameConcordionPairFromFixtureClass() {

        copyTestFixtureToConcordionProject("Spec.java");
        copySpecToConcordionProject("Spec.html");

        myFixture.renameElement(classInFile("/src/com/test/Spec.java"), "NewSpec");

        assertThat(findFileInProject("/src/com/test/Spec.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/Spec.html")).isNull();

        assertThat(findFileInProject("/src/com/test/NewSpec.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NewSpec.html")).isNotNull();
    }

    public void testRenameNonConcordionPairFromClass() {

        copyTestFixtureToConcordionProject("NoRunnerAnnotation.java");
        copySpecToConcordionProject("NoRunnerAnnotation.html");

        myFixture.renameElement(classInFile("/src/com/test/NoRunnerAnnotation.java"), "NewNoRunnerAnnotation");

        assertThat(findFileInProject("/src/com/test/NoRunnerAnnotation.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/NoRunnerAnnotation.html")).isNotNull();

        assertThat(findFileInProject("/src/com/test/NewNoRunnerAnnotation.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NewNoRunnerAnnotation.html")).isNull();
    }

    @Nullable
    private PsiClass classInFile(@NotNull String path) {
        return findChildOfType(findFileInProject(path), PsiClass.class);
    }
}