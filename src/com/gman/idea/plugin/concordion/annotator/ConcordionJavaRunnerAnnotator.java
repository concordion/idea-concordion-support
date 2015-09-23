package com.gman.idea.plugin.concordion.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionGutterRenderer.rendererForRunnerClass;

public class ConcordionJavaRunnerAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof PsiClass)
                || !(element.getParent() instanceof PsiFile)) {
            return;
        }

        PsiClass javaRunner = (PsiClass) element;
        PsiFile htmlSpec = correspondingHtmlSpec(javaRunner);

        PsiIdentifier className = PsiTreeUtil.getChildOfType(element, PsiIdentifier.class);

        if (htmlSpec != null && className != null) {
            holder
                    .createInfoAnnotation(className.getTextRange(), "")
                    .setGutterIconRenderer(rendererForRunnerClass(javaRunner, htmlSpec));
        }
    }
}
