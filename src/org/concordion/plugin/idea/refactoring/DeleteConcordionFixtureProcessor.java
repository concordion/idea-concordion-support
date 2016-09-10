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
import static org.concordion.plugin.idea.refactoring.ConcordionPairedFileType.SPEC;
import static org.concordion.plugin.idea.refactoring.ConcordionRefactoringDialogs.deletePaired;
import static org.concordion.plugin.idea.settings.ConcordionFilesRefactoring.*;

public class DeleteConcordionFixtureProcessor extends AbstractSafeDeleteProcessorDelegate {

    @Override
    public boolean handlesElement(PsiElement element) {
        return super.handlesElement(element) && element instanceof PsiClass && isConcordionFixture((PsiClass) element);
    }

    @Nullable
    @Override
    public Collection<PsiElement> getAdditionalElementsToDelete(@NotNull PsiElement element, @NotNull Collection<PsiElement> allElementsToDelete, final boolean askUser) {

        PsiClass fixture = (PsiClass) element;
        PsiFile spec = ConcordionNavigationService.getInstance(element.getProject()).correspondingSpec(fixture);

        if (spec == null) {
            return ImmutableList.of();
        }

        if (deletePaired(SPEC, refactoring) == BOTH) {
            return ImmutableList.of(spec);
        }

        return ImmutableSet.of();
    }
}
