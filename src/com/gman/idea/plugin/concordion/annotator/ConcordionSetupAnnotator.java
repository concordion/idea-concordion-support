package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.ConcordionSetup;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

@Deprecated
public final class ConcordionSetupAnnotator {

    private ConcordionSetupAnnotator() {
    }

    public static void annotateSetUpIssues(
            @NotNull PsiElement element,
            @NotNull AnnotationHolder holder,
            @NotNull ConcordionSetup setup
    ) {
        if (setup.javaRunner == null) {
            createError(holder, element, "Missing java runner");
        } else if (!setup.runWithAnnotationPresent) {
            createError(holder, element, "@RunWith annotation is missing");
        } else if (!setup.concordionRunnerPresent) {
            createError(holder, element, "@RunWith using runner incompatible with Concordion");
        }

        if (setup.htmlSpec == null) {
            createError(holder, element, "Missing html spec");
        } else if (!setup.specHasConcordionSchema) {
            createError(holder, element, "Missing concordion schema");
        }

    }

    private static void createError(AnnotationHolder holder, PsiElement element, String text) {
        holder
                .createErrorAnnotation(element, text)
                .setTooltip(text);
    }

}
