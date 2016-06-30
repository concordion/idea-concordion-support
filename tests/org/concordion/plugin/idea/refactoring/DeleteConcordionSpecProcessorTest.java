package org.concordion.plugin.idea.refactoring;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.safeDelete.SafeDeleteHandler;
import com.intellij.refactoring.safeDelete.SafeDeleteProcessor;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class DeleteConcordionSpecProcessorTest  extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/navigation";
    }

    public void testDeleteConcordionPairFromSpec() {

        copyTestFixtureToConcordionProject("Spec.java");
        copySpecToConcordionProject("Spec.html");

        SafeDeleteProcessor.createInstance(getProject(), null, new PsiElement[]{
                findFileInProject("/resources/com/test/Spec.html")
        }, false, false, true).run();

        assertThat(findFileInProject("/src/com/test/Spec.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/Spec.html")).isNull();
    }

    public void testDeleteNonConcordionPairFromHtml() {

        copyTestFixtureToConcordionProject("NoNamespace.java");
        copySpecToConcordionProject("NoNamespace.html");

        SafeDeleteProcessor.createInstance(getProject(), null, new PsiElement[]{
                findFileInProject("/resources/com/test/NoNamespace.html")
        }, false, false, true).run();

        assertThat(findFileInProject("/src/com/test/NoNamespace.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NoNamespace.html")).isNull();
    }
}