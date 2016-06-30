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

import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.isConcordionFixture;
import static org.concordion.plugin.idea.refactoring.ConcordionRefactoringDialogs.deletePairedFile;
import static org.concordion.plugin.idea.settings.ConcordionFilesRefactoring.BOTH;

public class DeleteConcordionFixtureProcessor extends AbstractSafeDeleteProcessorDelegate {

    @Override
    public boolean handlesElement(PsiElement element) {
        return element instanceof PsiClass && isConcordionFixture((PsiClass) element);
    }

    @Nullable
    @Override
    public Collection<PsiElement> getAdditionalElementsToDelete(@NotNull PsiElement element, @NotNull Collection<PsiElement> collection, boolean b) {

        PsiClass fixture = (PsiClass) element;
        PsiFile spec = ConcordionNavigationService.getInstance(element.getProject()).correspondingSpec(fixture);

        if (spec == null) {
            return ImmutableList.of();
        }

        if (deletePairedFile() == BOTH) {
            return ImmutableList.of(spec);
        }

        return ImmutableSet.of();
    }
}
