package org.concordion.plugin.idea.refactoring;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.safeDelete.SafeDeleteHandler;
import com.intellij.refactoring.safeDelete.SafeDeleteProcessor;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.concordion.plugin.idea.ConcordionPsiUtils.classIn;

public class DeleteConcordionFixtureProcessorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/navigation";
    }

    public void testDeleteConcordionPairFromFixtureClass() {

        copyTestFixtureToConcordionProject("Spec.java");
        copySpecToConcordionProject("Spec.html");

        SafeDeleteProcessor.createInstance(getProject(), null, new PsiElement[]{
                classIn(findFileInProject("/src/com/test/Spec.java"))
        }, false, false, true).run();

        assertThat(findFileInProject("/src/com/test/Spec.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/Spec.html")).isNull();
    }

    public void testDeleteNonConcordionPairFromClass() {

        copyTestFixtureToConcordionProject("NoRunnerAnnotation.java");
        copySpecToConcordionProject("NoRunnerAnnotation.html");

        SafeDeleteProcessor.createInstance(getProject(), null, new PsiElement[]{
                classIn(findFileInProject("/src/com/test/NoRunnerAnnotation.java"))
        }, false, false, true).run();

        assertThat(findFileInProject("/src/com/test/NoRunnerAnnotation.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/NoRunnerAnnotation.html")).isNotNull();
    }
}
