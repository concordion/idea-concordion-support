package com.gman.idea.plugin.concordion;

import com.intellij.psi.PsiClass;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PsiElementCacheTest extends TestCase {

    private final Map<String, PsiElementCache.CachedPsiElement<PsiClass>> spy = new HashMap<>();
    private final PsiElementCache<PsiClass> testingInstance = new PsiElementCache<>(PsiClass::getName, spy);

    private final String notAddedKey = "notAddedKey";
    private final String notAddedName = "notAddedName";
    private PsiClass notAddedPsiElement;

    private final String testKey = "testKey";
    private final String testName = "testName";
    private PsiClass testPsiElement;

    @Override
    protected void setUp() throws Exception {
        notAddedPsiElement = mock(PsiClass.class);
        when(notAddedPsiElement.getName()).thenReturn(notAddedName);
        when(notAddedPsiElement.isValid()).thenReturn(true);

        testPsiElement = mock(PsiClass.class);
        when(testPsiElement.getName()).thenReturn(testName);
        when(testPsiElement.isValid()).thenReturn(true);
        testingInstance.put(testKey, testPsiElement);
    }

    @Override
    public void tearDown() {
        spy.clear();
    }

    public void testMissingElementShouldBeNull() {
        assertThat(testingInstance.get(notAddedKey)).isNull();
    }

    public void testAddedElementShouldBeCached() {
        assertThat(testingInstance.get(testKey)).isNotNull().isSameAs(testPsiElement);
    }

    public void testAddedNullShouldNotBeCached() {
        int initialSize = spy.size();
        testingInstance.put(notAddedKey, null);
        assertThat(spy.size()).isEqualTo(initialSize);
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

    public void testNotUsingSupplierIfAlreadyCached() {
        assertThat(testingInstance.getOrCompute(testKey, () -> {
            throw new RuntimeException("should not be called");
        })).isSameAs(testPsiElement);
    }

    public void testComputeIfElementIsAbsent() {
        assertThat(testingInstance.getOrCompute(notAddedKey, () -> notAddedPsiElement)).isSameAs(notAddedPsiElement);
    }

    public void testComputedValueIsAddedToCache() {
        testingInstance.getOrCompute(notAddedKey, () -> notAddedPsiElement);
        assertThat(testingInstance.get(notAddedKey)).isSameAs(notAddedPsiElement);
    }
}