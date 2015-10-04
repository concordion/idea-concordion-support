package com.gman.idea.plugin.concordion;

import com.intellij.psi.PsiNamedElement;

import java.util.Map;
import java.util.WeakHashMap;

public class PsiElementCache<T extends PsiNamedElement> {

    private final Map<String, CachedPsiElement<T>> cache;

    public PsiElementCache() {
        this(new WeakHashMap<>());
    }

    public PsiElementCache(Map<String, CachedPsiElement<T>> cache) {
        this.cache = cache;
    }

    public T get(String key) {
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

    public void put(String key, T value) {
        cache.put(key, create(value));
    }

    public static <T extends PsiNamedElement> CachedPsiElement<T> create(T element) {
        return new CachedPsiElement<>(element.getName(), element);
    }

    public static class CachedPsiElement<T extends PsiNamedElement> {

        private final String originalName;
        private final T element;

        private CachedPsiElement(String originalName, T element) {
            this.originalName = originalName;
            this.element = element;
        }

        public boolean isValid() {
            return originalName.equals(element.getName()) && element.isValid();
        }

        public T get() {
            return element;
        }
    }
}
