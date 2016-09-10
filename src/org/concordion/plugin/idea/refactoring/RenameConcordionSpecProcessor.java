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

import static org.concordion.plugin.idea.ConcordionPsiUtils.removeExtension;
import static org.concordion.plugin.idea.refactoring.ConcordionPairedFileType.FIXTURE;
import static org.concordion.plugin.idea.refactoring.ConcordionRefactoringDialogs.renamePaired;
import static org.concordion.plugin.idea.settings.ConcordionFilesRefactoring.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specConfiguredInFile;

public class RenameConcordionSpecProcessor extends RenamePsiElementProcessor implements ConcordionSettingsListener {

    @NotNull
    private ConcordionFilesRefactoring refactoring = ASK;

    public RenameConcordionSpecProcessor() {
        registerListener();
    }

    @Override
    public void settingsChanged(@NotNull ConcordionSettings newState) {
        refactoring = newState.getRenamePairs();
    }

    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return refactoring != DISABLED && element instanceof PsiFile && specConfiguredInFile((PsiFile) element);
    }

    @Override
    public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope) {
        PsiFile spec = (PsiFile) element;
        PsiClass fixture = ConcordionNavigationService.getInstance(element.getProject()).correspondingTestFixture(spec);

        if (fixture == null) {
            return;
        }

        if (renamePaired(FIXTURE, refactoring) == BOTH) {
            allRenames.put(
                    fixture,
                    removeExtension(newName)
            );
        }
    }
}
