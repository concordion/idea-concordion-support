package org.concordion.plugin.idea;

import com.intellij.ide.fileTemplates.impl.DefaultTemplate;
import com.intellij.lang.Language;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public interface ConcordionExtension {

    @NotNull
    Language language();

    @NotNull
    Set<String> fileExtensions();

    @NotNull
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

    default boolean usableWith(@NotNull PsiFile file) {
        return fileExtensions().contains(file.getFileType().getDefaultExtension());
    }

    @NotNull
    static <T extends ConcordionExtension> Stream<T> extensions(
            @NotNull ExtensionPointName<T> extensionPoint
    ) {
        return stream(extensionPoint.getExtensions());
    }

    @NotNull
    static <T extends ConcordionExtension> Optional<T> extension(
            @NotNull ExtensionPointName<T> extensionPoint,
            @NotNull PsiFile file
    ) {
        return extensions(extensionPoint).filter(e -> e.usableWith(file)).findFirst();
    }
}
