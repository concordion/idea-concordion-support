package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.lang.psi.ConcordionField;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class UsingMapKeyAsField extends LocalInspectionTool {

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);

                if (element instanceof ConcordionField && ((ConcordionField) element).isKeyInMap()) {
                    holder.registerProblem(element, "Using map key as a field");
                }
            }
        };
    }
}
