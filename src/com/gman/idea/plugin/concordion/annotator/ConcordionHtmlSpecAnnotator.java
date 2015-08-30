package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.Concordion;
import com.gman.idea.plugin.concordion.ConcordionSetup;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionGutterRenderer.rendererForHtmlSpec;
import static com.gman.idea.plugin.concordion.ConcordionSetup.from;
import static com.gman.idea.plugin.concordion.annotator.ConcordionSetupAnnotator.annotateSetUpIssues;

public class ConcordionHtmlSpecAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof XmlAttributeValue)) {
            return;
        }
        XmlAttributeValue value = (XmlAttributeValue) element;
        if (!Concordion.NAMESPACE.equalsIgnoreCase(value.getValue())) {
            return;
        }

        PsiFile htmlSpec = element.getContainingFile();
        PsiClass javaRunner = correspondingJavaRunner(htmlSpec);

        ConcordionSetup setup = from(javaRunner, htmlSpec);

        annotateSetUpIssues(element, holder, setup);

        if (javaRunner != null) {
            holder
                    .createInfoAnnotation(element.getTextRange(), "")
                    .setGutterIconRenderer(rendererForHtmlSpec(htmlSpec, javaRunner));
        }
    }
}
