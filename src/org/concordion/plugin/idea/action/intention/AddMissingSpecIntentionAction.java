package org.concordion.plugin.idea.action.intention;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.concordion.plugin.idea.fixtures.ConcordionTestFixtures;
import org.concordion.plugin.idea.menu.ConcordionNewSpecAndFixtureDialog;
import org.concordion.plugin.idea.menu.ConcordionSpecAndFixtureCreationParameters;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.ConcordionPsiUtils.classIn;
import static org.concordion.plugin.idea.menu.ConcordionSpecAndFixtureCreationParameters.fromExistingFixture;

public class AddMissingSpecIntentionAction extends BaseIntentionAction {

    private static final String ACTION_NAME = "Add missing Concordion spec";

    public AddMissingSpecIntentionAction() {
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
        PsiClass aClass = classIn(file);
        return aClass != null
                && ConcordionTestFixtures.isConcordionFixture(aClass)
                && ConcordionNavigationService.getInstance(project).correspondingSpec(aClass) == null;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        new ConcordionNewSpecAndFixtureDialog(
                project, fromExistingFixture(classIn(file))

        ).showAndGet();
    }
}
