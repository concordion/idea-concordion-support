package org.concordion.plugin.idea;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcordionEditorUtil {

    @Nullable
    public static PsiFile findFileFrom(@NotNull Editor editor) {
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if (virtualFile == null
                || editor.getProject() == null) {
            return null;
        }
        return PsiManager.getInstance(editor.getProject()).findFile(virtualFile);
    }
}
