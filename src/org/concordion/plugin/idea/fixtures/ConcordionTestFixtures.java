package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.JVMElementFactory;
import com.intellij.psi.PsiClass;
import org.concordion.plugin.idea.action.quickfix.factories.ConcordionFixtureMemberFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

import static org.concordion.plugin.idea.ConcordionExtensionUtils.extension;

public final class ConcordionTestFixtures {

    private ConcordionTestFixtures() {
    }

    public static boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        return fixtureExtension(testFixture).map(fixture -> fixture.isConcordionFixture(testFixture)).orElse(false);
    }

    public static boolean fullOgnlEnabled(@NotNull PsiClass testFixture) {
        return fixtureExtension(testFixture).map(fixture -> fixture.fullOgnlEnabled(testFixture)).orElse(false);
    }

    @NotNull
    public static Set<String> configuredExtensions(@NotNull PsiClass testFixture) {
        return fixtureExtension(testFixture).map(fixture -> fixture.configuredExtensions(testFixture)).orElse(ImmutableSet.of());
    }

    @Nullable
    public static String extensionNamespace(@NotNull PsiClass testFixture) {
        return fixtureExtension(testFixture).map(fixture -> fixture.extensionNamespace(testFixture)).orElse(null);
    }

    @Nullable
    public static ConcordionFixtureMemberFactory memberFactory(@NotNull PsiClass testFixture) {
        return fixtureExtension(testFixture).map(fixture -> fixture.memberFactory(testFixture)).orElse(null);
    }

    @NotNull
    private static Optional<ConcordionTestFixture> fixtureExtension(@NotNull PsiClass testFixture) {
        return extension(ConcordionTestFixture.EP_NAME, testFixture.getContainingFile());
    }
}
