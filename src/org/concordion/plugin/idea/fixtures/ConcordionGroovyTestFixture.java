package org.concordion.plugin.idea.fixtures;

import org.jetbrains.plugins.groovy.GroovyFileType;
import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.NotNull;

public class ConcordionGroovyTestFixture extends ConcordionTestFixture {

    @NotNull
    @Override
    public FileType testFixtureFileType() {
        return GroovyFileType.GROOVY_FILE_TYPE;
    }
}
