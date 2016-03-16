package org.concordion.plugin.idea.reference;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.lang.psi.ConcordionField;
import org.concordion.plugin.idea.lang.psi.ConcordionMethod;
import org.concordion.plugin.idea.lang.psi.ConcordionVariable;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class ReferenceResolverInMdInjectionTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/reference";
    }

    public void testResolveField() {

        copyTestFixtureToConcordionProject("FieldReference.java");
        VirtualFile mdSpec = copySpecToConcordionProject("FieldReference.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        ConcordionField concordionField = elementUnderCaret();
        PsiField javaField = resolveReferences(concordionField);

        assertThat(javaField).isNotNull();
        assertThat(javaField.getName()).isEqualTo("propertyToResolve");
    }

    public void testResolveMethod() {

        copyTestFixtureToConcordionProject("MethodReference.java");
        VirtualFile mdSpec = copySpecToConcordionProject("MethodReference.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        ConcordionMethod concordionMethod = elementUnderCaret();
        PsiMethod method = resolveReferences(concordionMethod);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("methodToResolve");
    }

    public void testResolveVariable() {

        copyTestFixtureToConcordionProject("VariableReference.java");
        VirtualFile mdSpec = copySpecToConcordionProject("VariableReference.md");

        myFixture.configureFromExistingVirtualFile(mdSpec);

        ConcordionVariable variable = elementUnderCaret();
        ConcordionVariable declaration = resolveReferences(variable);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getName()).isEqualTo("var");
        assertThat(declaration).isNotEqualTo(variable);
    }
}
