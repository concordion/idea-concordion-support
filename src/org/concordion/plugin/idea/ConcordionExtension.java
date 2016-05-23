package org.concordion.plugin.idea;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public interface ConcordionExtension {

    @NotNull
    Set<String> fileExtensions();

    @NotNull
    default Stream<String> fileExtensionsAsStream() {
        return fileExtensions().stream();
    }

    default boolean usableWith(@NotNull String fileExtension) {
        return fileExtensions().contains(fileExtension);
    }

    default boolean usableWith(@NotNull PsiFile file) {
        return fileExtensions().contains(file.getFileType().getDefaultExtension());
    }
}
