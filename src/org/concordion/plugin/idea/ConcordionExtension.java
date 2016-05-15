package org.concordion.plugin.idea;

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
}
