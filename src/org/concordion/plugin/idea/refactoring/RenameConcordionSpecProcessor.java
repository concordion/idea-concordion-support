package org.concordion.plugin.idea.refactoring;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specConfiguredInFile;

public class RenameConcordionSpecProcessor extends RenamePsiElementProcessor {

    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return element instanceof PsiFile && specConfiguredInFile((PsiFile) element);
    }

    @Override
    public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope) {
        PsiFile spec = (PsiFile) element;
        PsiClass fixture = ConcordionNavigationService.getInstance(element.getProject()).correspondingTestFixture(spec);

        if (fixture == null) {
            return;
        }

        allRenames.put(
                fixture,
                removeExtension(newName)
        );
    }
}
