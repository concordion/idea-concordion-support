package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static org.concordion.plugin.idea.ConcordionContextKeys.*;

public enum ConcordionSpecType {

    HTML("html", "xhtml") {
        @Override
        public boolean configuredIn(@NotNull PsiFile file, @Nullable ProcessingContext context) {
            String prefix = Namespaces.CONCORDION.prefixInFile(file);
            if (context != null) {
                context.put(CONCORDION_SCHEMA_PREFIX, prefix);
            }
            return prefix != null;
        }

        @Override
        public boolean extensionsConfiguredIn(@NotNull PsiFile file, @Nullable ProcessingContext context) {
            String prefix = Namespaces.CONCORDION_EXTENSIONS.prefixInFile(file);
            if (context != null) {
                context.put(CONCORDION_EXTENSIONS_SCHEMA_PREFIX, prefix);
            }
            return prefix != null;
        }
    },
    MD("md", "markdown") {
        @Override
        public boolean configuredIn(@NotNull PsiFile file, @Nullable ProcessingContext context) {
            if (context != null) {
                context.put(CONCORDION_SCHEMA_PREFIX, "c");
            }
            return true;
        }

        @Override
        public boolean extensionsConfiguredIn(@NotNull PsiFile file, @Nullable ProcessingContext context) {
            return false;
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
        return configuredIn(file, null);
    }

    /**
     * Must specify CONCORDION_SCHEMA_PREFIX in context
     */
    public abstract boolean configuredIn(@NotNull PsiFile file, @Nullable ProcessingContext context);

    /**
     * Must specify CONCORDION_EXTENSIONS_SCHEMA_PREFIX in context
     */
    public abstract boolean extensionsConfiguredIn(@NotNull PsiFile file, @Nullable ProcessingContext context);

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
