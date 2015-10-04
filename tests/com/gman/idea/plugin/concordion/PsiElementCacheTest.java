package com.gman.idea.plugin.concordion;

import com.intellij.psi.PsiNamedElement;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PsiElementCacheTest extends TestCase {

    private final Map<String, PsiElementCache.CachedPsiElement<PsiNamedElement>> spy = new HashMap<>();
    private final PsiElementCache<PsiNamedElement> testingInstance = new PsiElementCache<>(spy);

    private final String testKey = "testKey";
    private final String testName = "testName";
    private PsiNamedElement testPsiElement;

    @Override
    protected void setUp() throws Exception {
        testPsiElement = mock(PsiNamedElement.class);
        when(testPsiElement.getName()).thenReturn(testName);
        when(testPsiElement.isValid()).thenReturn(true);
        testingInstance.put(testKey, testPsiElement);
    }

    @Override
    public void tearDown() {
        spy.clear();
    }

    public void testMissingElementShouldBeNull() {
        assertThat(testingInstance.get("notThereYet")).isNull();
    }

    public void testAddedElementShouldBeCached() {
        assertThat(testingInstance.get(testKey)).isNotNull().isSameAs(testPsiElement);
    }

    public void testRenamedElementShouldBeEvicted() {
        when(testPsiElement.getName()).thenReturn("newTestName");
        assertThat(testingInstance.get(testKey)).isNull();
        assertThat(spy.get(testKey)).isNull();
    }

    public void testInvalidElementShouldBeEvicted() {
        when(testPsiElement.isValid()).thenReturn(false);
        assertThat(testingInstance.get(testKey)).isNull();
        assertThat(spy.get(testKey)).isNull();
    }
}