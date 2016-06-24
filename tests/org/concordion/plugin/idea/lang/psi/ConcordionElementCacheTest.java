package org.concordion.plugin.idea.lang.psi;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionElementCacheTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/lang";
    }

    public void testFieldRenamed() {

        VirtualFile fixture = copyTestFixtureToConcordionProject("CacheEviction.java");
        VirtualFile spec = copySpecToConcordionProject("CacheEviction.html", "field");

        myFixture.configureFromExistingVirtualFile(spec);

        ConcordionPsiElement element = elementUnderCaret();
        assertResolved(element, "com.test.CacheEviction.Nested");

        writeAction(() -> {
            find(PsiField.class, fixture).setName("newField");
            return null;
        });

        assertUnresolved(element);
    }

    public void testFieldTypeChanged() {

        VirtualFile fixture = copyTestFixtureToConcordionProject("CacheEviction.java");
        VirtualFile spec = copySpecToConcordionProject("CacheEviction.html", "field");

        myFixture.configureFromExistingVirtualFile(spec);

        ConcordionPsiElement element = elementUnderCaret();
        assertResolved(element, "com.test.CacheEviction.Nested");

        writeAction(() -> {
            find(PsiField.class, fixture).getTypeElement().replace(newType(PsiType.INT));
            return null;
        });

        assertResolved(element, PsiType.INT.getCanonicalText());
    }

    public void testMethodRenamed() {

        VirtualFile fixture = copyTestFixtureToConcordionProject("CacheEviction.java");
        VirtualFile spec = copySpecToConcordionProject("CacheEviction.html", "method()");

        myFixture.configureFromExistingVirtualFile(spec);

        ConcordionPsiElement element = elementUnderCaret();
        assertResolved(element, "com.test.CacheEviction.Nested");

        writeAction(() -> {
            find(PsiMethod.class, fixture).setName("newMethod");
            return null;
        });

        assertUnresolved(element);
    }

    public void testMethodTypeChanged() {

        VirtualFile fixture = copyTestFixtureToConcordionProject("CacheEviction.java");
        VirtualFile spec = copySpecToConcordionProject("CacheEviction.html", "method()");

        myFixture.configureFromExistingVirtualFile(spec);

        ConcordionPsiElement element = elementUnderCaret();
        assertResolved(element, "com.test.CacheEviction.Nested");

        writeAction(() -> {
            find(PsiMethod.class, fixture).getReturnTypeElement().replace(newType(PsiType.INT));
            return null;
        });

        assertResolved(element, PsiType.INT.getCanonicalText());
    }

    public void testMethodParametersChanged() {

        VirtualFile fixture = copyTestFixtureToConcordionProject("CacheEviction.java");
        VirtualFile spec = copySpecToConcordionProject("CacheEviction.html", "method()");

        myFixture.configureFromExistingVirtualFile(spec);

        ConcordionPsiElement element = elementUnderCaret();
        assertResolved(element, "com.test.CacheEviction.Nested");

        writeAction(() -> {
            find(PsiMethod.class, fixture).getParameterList().add(newParameter(PsiType.INT, "i"));
            return null;
        });

        assertUnresolved(element);
    }

    public void testParentRenamed() {

        VirtualFile fixture = copyTestFixtureToConcordionProject("CacheEviction.java");
        VirtualFile spec = copySpecToConcordionProject("CacheEviction.html", "method().chained");

        myFixture.configureFromExistingVirtualFile(spec);

        ConcordionPsiElement element = elementUnderCaret();
        assertResolved(element, "java.lang.String");

        writeAction(() -> {
            find(PsiMethod.class, fixture).setName("newMethod");
            return null;
        });

        assertUnresolved(element);
    }

    public void testParentTypeChanged() {

        VirtualFile fixture = copyTestFixtureToConcordionProject("CacheEviction.java");
        VirtualFile spec = copySpecToConcordionProject("CacheEviction.html", "method().chained");

        myFixture.configureFromExistingVirtualFile(spec);

        ConcordionPsiElement element = elementUnderCaret();
        assertResolved(element, "java.lang.String");

        writeAction(() -> {
            find(PsiMethod.class, fixture).getReturnTypeElement().replace(newType(PsiType.INT));
            return null;
        });

        assertUnresolved(element);
    }


    private void assertUnresolved(@Nullable ConcordionPsiElement element) {
        assertThat(element).isNotNull();
        assertThat(element.isResolvable()).isFalse();
        assertThat(element.getType()).isNull();
    }

    private void assertResolved(@Nullable ConcordionPsiElement element, @NotNull String type) {
        assertThat(element).isNotNull();
        assertThat(element.isResolvable()).isTrue();
        assertThat(element.getType()).isNotNull();
        assertThat(element.getType().getCanonicalText()).isEqualTo(type);
    }

    @Nullable
    private <T extends PsiElement> T find(@NotNull Class<T> type, @NotNull VirtualFile file) {
        return findChildOfType(PsiManager.getInstance(getProject()).findFile(file), type);
    }

    @NotNull
    private PsiTypeElement newType(@NotNull PsiType type) {
        return JavaPsiFacade.getInstance(getProject()).getElementFactory().createTypeElement(type);
    }

    @NotNull
    private PsiParameter newParameter(@NotNull PsiType type, @NotNull String name) {
        return JavaPsiFacade.getInstance(getProject()).getElementFactory().createParameter(name, type);
    }
}
