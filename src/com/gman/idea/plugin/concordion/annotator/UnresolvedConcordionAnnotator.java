package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionPsiElement;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.correspondingJavaRunner;
import static com.gman.idea.plugin.concordion.ConcordionInjectionUtils.getTopLevelFile;

public abstract class UnresolvedConcordionAnnotator<T extends ConcordionPsiElement> implements Annotator {

    private final Class<T> type;

    public UnresolvedConcordionAnnotator(Class<T> type) {
        this.type = type;
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!type.isAssignableFrom(element.getClass())) {
            return;
        }
        T concordionPsiElement = (T) element;
        if (concordionPsiElement.isResolvable()) {
            return;
        }

        PsiFile htmlSpec = getTopLevelFile(element);
        PsiClass javaRunner = correspondingJavaRunner(htmlSpec);
        if (htmlSpec == null || javaRunner == null) {
            return;
        }

        reportUnresolved(concordionPsiElement, holder, htmlSpec, javaRunner);
    }

    protected abstract void reportUnresolved(
            @NotNull T concordionPsiElement,
            @NotNull AnnotationHolder holder,
            @NotNull PsiFile htmlSpec,
            @NotNull PsiClass javaRunner
    );
}
