package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public enum ConcordionSpecType {

    HTML("html", "xhtml"),
    MD("md", "markdown");

    @NotNull
    public final Set<String> extensions;

    ConcordionSpecType(String... extensions) {
        this.extensions = ImmutableSet.copyOf(extensions);
    }

    public boolean canBeIn(@NotNull PsiFile file) {
        return extensions.contains(file.getFileType().getDefaultExtension());
    }
}
