package com.gman.idea.plugin.concordion.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionGutterRenderer.rendererForHtmlSpec;

public class ConcordionHtmlSpecAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!isConcordionHtmlSpec(element.getContainingFile())
                || !(element instanceof XmlAttributeValue)
                || !isConcordionNamespace(((XmlAttributeValue) element).getValue())) {
            return;
        }

        PsiFile htmlSpec = element.getContainingFile();
        PsiClass javaRunner = correspondingJavaRunner(htmlSpec);

        if (javaRunner != null) {
            holder
                    .createInfoAnnotation(element.getTextRange(), "")
                    .setGutterIconRenderer(rendererForHtmlSpec(htmlSpec, javaRunner));
        }
    }
}
