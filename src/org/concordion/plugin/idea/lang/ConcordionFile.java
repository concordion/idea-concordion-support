package org.concordion.plugin.idea.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;


public class ConcordionFile extends PsiFileBase {
    public ConcordionFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ConcordionLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ConcordionFileType.INSTANCE;
    }
}
