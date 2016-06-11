package org.concordion.plugin.idea;

import com.intellij.ide.fileTemplates.impl.DefaultTemplate;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public interface ConcordionExtension {

    @NotNull
    Language language();

    @NotNull
    Set<String> fileExtensions();

    @Nullable
    default DefaultTemplate template() {
        String name = language().getDisplayName();
        String extension = ofNullable(language().getAssociatedFileType()).map(FileType::getDefaultExtension).orElse("");

        URL template = ConcordionExtension.class.getResource("/templates/" + name + ".ft");
        if (template == null) {
            throw new IllegalStateException("Template not found: /templates/" + name + ".ft");
        }
        return new DefaultTemplate(
                name,
                extension,
                template,
                null
        );
    }

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
