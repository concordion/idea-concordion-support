package com.gman.idea.plugin.concordion;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class PsiElementCached<T extends PsiElement & NavigationItem> {

    private static final String KEY = "this";
    private final PsiElementCache<T> cache;

    public PsiElementCached(@NotNull Function<T, String> identityFunction) {
        this.cache = new PsiElementCache<>(identityFunction);
    }

    public void set(T value) {
        cache.put(KEY, value);
    }

    public T get() {
        return cache.get(KEY);
    }

    public T getOrCompute(Supplier<T> supplier) {
        return cache.getOrCompute(KEY, supplier);
    }
}
