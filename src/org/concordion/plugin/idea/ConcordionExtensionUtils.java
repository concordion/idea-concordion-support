package org.concordion.plugin.idea;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public final class ConcordionExtensionUtils {

    private ConcordionExtensionUtils() {
    }

    @NotNull
    public static <T extends ConcordionExtension> Set<String> allRegisteredExtensions(@NotNull ExtensionPointName<T> extensionPoint) {
        return stream(extensionPoint.getExtensions())
                .flatMap(ConcordionExtension::fileExtensionsAsStream)
                .collect(toSet());
    }

    @Nullable
    public static <T extends ConcordionExtension> T extension(
            @NotNull ExtensionPointName<T> extensionPoint,
            @NotNull String fileType
    ) {
        return stream(extensionPoint.getExtensions())
                .filter(e -> e.usableWith(fileType))
                .findFirst().orElse(null);
    }

    @Nullable
    public static <T extends ConcordionExtension> T extension(
            @NotNull ExtensionPointName<T> extensionPoint,
            @NotNull PsiFile fileType
    ) {
        return extension(extensionPoint, fileType.getFileType().getDefaultExtension());
    }
}
