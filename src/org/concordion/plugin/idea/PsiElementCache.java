package org.concordion.plugin.idea;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class PsiElementCache<T extends PsiElement> {

    @NotNull private final Function<T, String> identityFunction;
    @NotNull private final Map<String, CachedPsiElement> cache = new HashMap<>();

    public PsiElementCache(@NotNull Function<T, String> identityFunction) {
        this.identityFunction = identityFunction;
    }

    public void put(@NotNull String key, @Nullable T element) {
        if (element != null) {
            cache.put(key, new CachedPsiElement(element));
        }
    }

    @Nullable
    public T get(@NotNull String key) {
        CachedPsiElement cached = cache.get(key);
        if (cached == null) {
            return null;
        }
        T t = cached.getIfValid();
        if (t == null) {
            cache.remove(key);
            return null;
        }
        return t;
    }

    @Nullable
    public T getOrCompute(@NotNull String key, @NotNull Supplier<T> supplier) {
        T t = get(key);
        if (t == null) {
            t = supplier.get();
            put(key, t);
        }
        return t;
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    private class CachedPsiElement {

        @NotNull private final WeakReference<T> element;
        @Nullable private final String originalIdentity;

        public CachedPsiElement(@NotNull T element) {
            this.element = new WeakReference<>(element);
            this.originalIdentity = identityFunction.apply(element);
        }

        @Nullable
        public T getIfValid() {
            T elementRef = element.get();
            if (elementRef == null
                    || !Objects.equals(originalIdentity, identityFunction.apply(elementRef))
                    || !elementRef.isValid()) {
                return null;
            }
            return elementRef;
        }
    }
}
