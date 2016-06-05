package org.concordion.plugin.idea;

import com.intellij.psi.PsiClass;
import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PsiElementCacheTest extends TestCase {

    private final PsiElementCache<PsiClass> cache = new PsiElementCache<>(PsiClass::getName);

    private PsiClass notInCacheElement;
    private PsiClass inCacheElement;

    @Override
    protected void setUp() throws Exception {
        notInCacheElement = mock(PsiClass.class);
        when(notInCacheElement.getName()).thenReturn("notInCacheElement");
        when(notInCacheElement.isValid()).thenReturn(true);

        inCacheElement = mock(PsiClass.class);
        when(inCacheElement.getName()).thenReturn("inCacheElement");
        when(inCacheElement.isValid()).thenReturn(true);
        //WeakReference in cache will not lose this element as it is stored in inCacheElement link and in mockito internals
        cache.put("inCache", inCacheElement);
    }

    @Override
    public void tearDown() {
        cache.clear();
    }

    public void testMissingElementShouldBeNull() {
        assertThat(cache.get("notInCache")).isNull();
    }

    public void testAddedElementShouldBeCached() {
        assertThat(cache.get("inCache")).isNotNull().isSameAs(inCacheElement);
    }

    public void testAddedNullShouldNotBeCached() {
        int initialSize = cache.size();
        cache.put("notInCache", null);
        assertThat(cache.size()).isEqualTo(initialSize);
    }

    public void testRenamedElementShouldBeEvicted() {
        int initialSize = cache.size();
        when(inCacheElement.getName()).thenReturn("newInCacheElement");
        assertThat(cache.get("inCache")).isNull();
        assertThat(cache.size()).isEqualTo(initialSize - 1);
    }

    public void testInvalidElementShouldBeEvicted() {
        int initialSize = cache.size();
        when(inCacheElement.isValid()).thenReturn(false);
        assertThat(cache.get("inCache")).isNull();
        assertThat(cache.size()).isEqualTo(initialSize - 1);
    }

    public void testNotUsingSupplierIfAlreadyCached() {
        assertThat(cache.getOrCompute("inCache", () -> {
            throw new RuntimeException("should not be called");
        })).isSameAs(inCacheElement);
    }

    public void testComputeIfElementIsAbsent() {
        assertThat(cache.getOrCompute("notInCache", () -> notInCacheElement)).isSameAs(notInCacheElement);
    }

    public void testComputedValueIsAddedToCache() {
        int initialSize = cache.size();
        cache.getOrCompute("notInCache", () -> notInCacheElement);
        assertThat(cache.size()).isEqualTo(initialSize + 1);
    }
}
