package org.concordion.plugin.idea.refactoring;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.concordion.plugin.idea.settings.ConcordionFilesRefactoring;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.concordion.plugin.idea.settings.ConcordionSettingsListener;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.concordion.plugin.idea.ConcordionPsiUtils.getExtension;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.isConcordionFixture;
import static org.concordion.plugin.idea.refactoring.ConcordionRefactoringDialogs.renamePairedFile;
import static org.concordion.plugin.idea.settings.ConcordionFilesRefactoring.*;

public class RenameConcordionFixtureProcessor extends RenamePsiElementProcessor implements ConcordionSettingsListener {

    @NotNull
    private ConcordionFilesRefactoring refactoring = ASK;

    public RenameConcordionFixtureProcessor() {
        registerListener();
    }

    @Override
    public void settingsChanged(@NotNull ConcordionSettings newState) {
        refactoring = newState.getRenamePairs();
    }

    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return refactoring != DISABLED && element instanceof PsiClass && isConcordionFixture((PsiClass) element);
    }

    @Override
    public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope) {
        PsiClass fixture = (PsiClass) element;
        PsiFile spec = ConcordionNavigationService.getInstance(element.getProject()).correspondingSpec(fixture);

        if (spec == null) {
            return;
        }

        if (renamePairedFile(refactoring) == BOTH) {
            allRenames.put(
                    spec,
                    newName + '.' + getExtension(spec.getName())
            );
        }
    }
}
