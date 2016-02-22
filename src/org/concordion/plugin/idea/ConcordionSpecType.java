package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public enum ConcordionSpecType {

    MD("md", "markdown"),
    HTML("html", "xhtml") {
        @Override
        public boolean configuredIn(@NotNull PsiFile file) {
            return Namespaces.CONCORDION.prefixInFile(file) != null;
        }
    };

    @NotNull
    public final Set<String> extensions;

    ConcordionSpecType(String... extensions) {
        this.extensions = ImmutableSet.copyOf(extensions);
    }

    public boolean canBeIn(@NotNull PsiFile file) {
        return extensions.contains(file.getFileType().getDefaultExtension());
    }

    public boolean configuredIn(@NotNull PsiFile file) {
        return canBeIn(file);
    }

    @Nullable
    public static ConcordionSpecType inFile(@NotNull PsiFile file) {
        for (ConcordionSpecType type : values()) {
            if (type.extensions.contains(file.getFileType().getDefaultExtension())) {
                return type;
            }
        }
        return null;
    }

    public static boolean specConfiguredInFile(@NotNull PsiFile file) {
        ConcordionSpecType type = inFile(file);
        return type != null && type.configuredIn(file);
    }
}
