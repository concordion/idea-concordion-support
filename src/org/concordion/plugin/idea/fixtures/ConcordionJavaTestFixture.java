package org.concordion.plugin.idea.fixtures;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.NotNull;

public class ConcordionJavaTestFixture extends ConcordionTestFixture {

    @NotNull
    @Override
    public FileType testFixtureFileType() {
        return JavaFileType.INSTANCE;
    }
}
