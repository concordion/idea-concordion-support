package org.concordion.plugin.idea.lang;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;

public final class ConcordionElementFactory {

    private ConcordionElementFactory() {
    }

    @NotNull
    public static PsiElement createIdentifier(@NotNull Project project, @NotNull String name) {
        PsiFile dummy = PsiFileFactory.getInstance(project).createFileFromText("dummy", ConcordionFileType.INSTANCE, name);
        //file -> ognlExpressionStart -> field -> identifier
        return dummy.getFirstChild().getFirstChild().getFirstChild();
    }
}
