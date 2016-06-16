package org.concordion.plugin.idea.refactoring;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.isConcordionFixture;

public class RenameConcordionFixtureProcessor extends RenamePsiElementProcessor {

    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return element instanceof PsiClass && isConcordionFixture((PsiClass) element);
    }

    @Override
    public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope) {
        PsiClass fixture = (PsiClass) element;
        PsiFile spec = ConcordionNavigationService.getInstance(element.getProject()).correspondingSpec(fixture);

        if (spec == null) {
            return;
        }

        String extension = getExtension(spec.getName());

        allRenames.put(
                spec,
                newName + '.' + extension
        );
    }
}
