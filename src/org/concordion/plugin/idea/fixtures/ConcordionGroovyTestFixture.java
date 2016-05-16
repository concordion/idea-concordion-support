package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ConcordionGroovyTestFixture implements ConcordionTestFixture {

    @NotNull
    @Override
    public Set<String> fileExtensions() {
        return ImmutableSet.of("groovy");
    }

    @Override
    public boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        return false;
    }

    @Override
    public boolean fullOgnlEnabled(@NotNull PsiClass testFixture) {
        return false;
    }

    @NotNull
    @Override
    public Set<String> configuredExtensions(@NotNull PsiClass testFixture) {
        return ImmutableSet.of();
    }

    @Nullable
    @Override
    public String extensionNamespace(@NotNull PsiClass testFixture) {
        return null;
    }
}
