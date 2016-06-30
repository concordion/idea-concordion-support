package org.concordion.plugin.idea.refactoring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static org.concordion.plugin.idea.refactoring.ConcordionRefactoringDialogs.deletePairedFile;
import static org.concordion.plugin.idea.settings.ConcordionFilesRefactoring.BOTH;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specConfiguredInFile;

public class DeleteConcordionSpecProcessor extends AbstractSafeDeleteProcessorDelegate {

    @Override
    public boolean handlesElement(PsiElement element) {
        return element instanceof PsiFile && specConfiguredInFile((PsiFile) element);
    }

    @Nullable
    @Override
    public Collection<PsiElement> getAdditionalElementsToDelete(@NotNull PsiElement element, @NotNull Collection<PsiElement> collection, boolean b) {

        PsiFile spec = (PsiFile) element;
        PsiClass fixture = ConcordionNavigationService.getInstance(element.getProject()).correspondingTestFixture(spec);

        if (fixture == null) {
            return ImmutableList.of();
        }

        if (deletePairedFile() == BOTH) {
            return ImmutableList.of(fixture);
        }

        return ImmutableSet.of();
    }
}
