package org.concordion.plugin.idea.fixtures;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public abstract class ConcordionTestFixture {

    @NotNull
    public static final ExtensionPointName<ConcordionTestFixture> EP_NAME = ExtensionPointName.create("org.concordion.plugin.idea.lang.testFixture");

    @NotNull
    public abstract FileType testFixtureFileType();

    @NotNull
    public static Collection<String> allPossibleFixtureExtensions() {
        return stream(Extensions.getExtensions(ConcordionTestFixture.EP_NAME))
                .map(ConcordionTestFixture::testFixtureFileType)
                .map(FileType::getDefaultExtension)
                .collect(toList());
    }
}
