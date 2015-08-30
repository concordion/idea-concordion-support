package com.gman.idea.plugin.concordion;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gman.idea.plugin.concordion.Concordion.*;

public class ConcordionSetup {

    @Nullable public final PsiClass javaRunner;
    @Nullable public final PsiFile htmlSpec;
    public final boolean runWithAnnotationPresent;
    public final boolean concordionRunnerPresent;
    public final boolean specHasConcordionSchema;

    protected ConcordionSetup(
            @Nullable PsiClass javaRunner,
            @Nullable PsiFile htmlSpec,
            boolean runWithAnnotationPresent,
            boolean concordionRunnerPresent,
            boolean specHasConcordionSchema
    ) {
        this.javaRunner = javaRunner;
        this.htmlSpec = htmlSpec;
        this.runWithAnnotationPresent = runWithAnnotationPresent;
        this.concordionRunnerPresent = concordionRunnerPresent;
        this.specHasConcordionSchema = specHasConcordionSchema;
    }

    public boolean isValid() {
        return javaRunner != null
                && htmlSpec != null
                && runWithAnnotationPresent
                && concordionRunnerPresent
                && specHasConcordionSchema;
    }

    @NotNull
    public static ConcordionSetup from(@Nullable PsiClass javaRunner, @Nullable PsiFile htmlSpec) {
        boolean runWithAnnotationPresent = false;
        boolean concordionRunnerPresent = false;
        boolean specHasConcordionSchema = false;

        if (javaRunner != null) {
            PsiAnnotation junitRunWithAnnotation = findJunitRunWithAnnotation(javaRunner);
            if (junitRunWithAnnotation != null) {
                runWithAnnotationPresent = true;
                concordionRunnerPresent = isRunWithAnnotationUsesConcordionRunner(junitRunWithAnnotation);
            }
        }

        if (htmlSpec != null) {
            specHasConcordionSchema = concordionSchemaPrefixOf(htmlSpec) != null;
        }

        return new ConcordionSetup(javaRunner, htmlSpec, runWithAnnotationPresent, concordionRunnerPresent, specHasConcordionSchema);
    }
}
