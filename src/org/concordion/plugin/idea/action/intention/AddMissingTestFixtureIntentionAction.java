package org.concordion.plugin.idea.action.intention;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.concordion.plugin.idea.menu.ConcordionNewSpecAndFixtureDialog;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.menu.ConcordionSpecAndFixtureCreationParameters.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specConfiguredInFile;

public class AddMissingTestFixtureIntentionAction extends BaseIntentionAction {

    private static final String ACTION_NAME = "Add missing concordion test fixture";

    public AddMissingTestFixtureIntentionAction() {
        setText(ACTION_NAME);
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return ACTION_NAME;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return specConfiguredInFile(file)
                && ConcordionNavigationService.getInstance(project).correspondingTestFixture(file) == null;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        new ConcordionNewSpecAndFixtureDialog(
                project, fromExistingSpec(file)

        ).showAndGet();
    }
}
