package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.concordion.plugin.idea.ConcordionTestFixtureUtil.extensionNamespace;

public enum ConcordionSpecType {

    HTML("html", "xhtml") {
        @Nullable
        @Override
        public String prefix(@NotNull PsiFile spec) {
            return Namespaces.CONCORDION.prefixInFile(spec);
        }

        @Nullable
        @Override
        public String extensionPrefix(@NotNull PsiFile spec) {
            return Namespaces.CONCORDION_EXTENSIONS.prefixInFile(spec);
        }
    },
    MD("md", "markdown") {
        @Nullable
        @Override
        public String prefix(@NotNull PsiFile spec) {
            return "c";
        }

        @Nullable
        @Override
        public String extensionPrefix(@NotNull PsiFile spec) {
            PsiClass fixture = ConcordionNavigationService.getInstance(spec.getProject()).correspondingTestFixture(spec);
            return fixture != null ? extensionNamespace(fixture) : null;
        }
    };

    @NotNull
    public final Set<String> extensions;

    ConcordionSpecType(@NotNull String... extensions) {
        this.extensions = ImmutableSet.copyOf(extensions);
    }

    public boolean canBeIn(@NotNull PsiFile file) {
        return extensions.contains(file.getFileType().getDefaultExtension());
    }

    @Nullable
    public abstract String prefix(@NotNull PsiFile file);

    @Nullable
    public abstract String extensionPrefix(@NotNull PsiFile file);

    @NotNull
    private static Optional<ConcordionSpecType> typeInFile(@NotNull PsiFile file) {
        return stream(values())
                .filter(type -> type.canBeIn(file))
                .findFirst();
    }

    @Nullable
    public static String prefixInFile(@NotNull PsiFile file) {
        return typeInFile(file).map(type -> type.prefix(file)).orElse(null);
    }

    @Nullable
    public static String extensionPrefixInFile(@NotNull PsiFile file) {
        return typeInFile(file).map(type -> type.extensionPrefix(file)).orElse(null);
    }

    public static boolean specConfiguredInFile(@NotNull PsiFile file) {
        return prefixInFile(file) != null;
    }

    @NotNull
    public static Set<String> allPossibleSpecExtensions() {
        return stream(values())
                .flatMap(type -> type.extensions.stream())
                .collect(toSet());
    }
}
