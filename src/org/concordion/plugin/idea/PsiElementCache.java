package org.concordion.plugin.idea;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class PsiElementCache<T extends PsiElement> {

    private final Function<T, String> identityFunction;
    private final Map<String, CachedPsiElement<T>> cache;

    public PsiElementCache(@NotNull Function<T, String> identityFunction) {
        this(identityFunction, new WeakHashMap<>());
    }

    public PsiElementCache(@NotNull Function<T, String> identityFunction, @NotNull Map<String, CachedPsiElement<T>> cache) {
        this.identityFunction = identityFunction;
        this.cache = cache;
    }

    public void put(@NotNull String key, @Nullable T value) {
        if (value != null) {
            cache.put(key, new CachedPsiElement<>(identityFunction, value));
        }
    }

    @Nullable
    public T get(@NotNull String key) {
        CachedPsiElement<T> cached = cache.get(key);
        if (cached == null) {
            return null;
        }
        if (!cached.isValid()) {
            cache.remove(key);
            return null;
        }
        return cached.get();
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

    protected static class CachedPsiElement<T extends PsiElement> {

        private final Function<T, String> identityFunction;
        private final T element;
        private final String identity;

        public CachedPsiElement(Function<T, String> identityFunction, T element) {
            this.identityFunction = identityFunction;
            this.element = element;
            this.identity = identityFunction.apply(element);
        }

        public boolean isValid() {
            return Objects.equals(identity, identityFunction.apply(element)) && element.isValid();
        }

        public T get() {
            return element;
        }
    }
}
