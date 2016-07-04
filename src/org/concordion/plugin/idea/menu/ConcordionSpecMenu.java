package org.concordion.plugin.idea.menu;

import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.ide.projectView.impl.ModuleGroup;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import org.concordion.plugin.idea.ConcordionExtension;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.fixtures;
import static org.concordion.plugin.idea.menu.ConcordionSpecAndFixtureCreationParameters.fromScratch;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specifications;

public class ConcordionSpecMenu extends DefaultActionGroup {

    public ConcordionSpecMenu() {
        setPopup(true);
        Presentation presentation = getTemplatePresentation();
        presentation.setText("Concordion");
        presentation.setIcon(ConcordionIcons.ICON);

        add(specAndFixtureDialog());
        add(createTemplatesMenuFor(fixtures(), "Fixture"));
        add(createTemplatesMenuFor(specifications(), "Specification"));
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setVisible(canCreate(e.getDataContext()));
    }

    private boolean canCreate(DataContext dataContext) {
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (project == null || view == null) {
            return false;
        }
        PsiDirectory[] directories = view.getDirectories();
        return directories.length == 1
                && JavaDirectoryService.getInstance().getPackage(directories[0]) != null;
    }

    @NotNull
    private static AnAction specAndFixtureDialog() {
        return new AnAction("Spec and fixture") {
            @Override
            public void actionPerformed(AnActionEvent event) {
                if (event.getProject() == null) {
                    return;
                }
                new ConcordionNewSpecAndFixtureDialog(
                        event.getProject(), fromScratch(event)
                ).showDialog();
            }
        };
    }

    @NotNull
    private static DefaultActionGroup createTemplatesMenuFor(
            @NotNull Stream<? extends ConcordionExtension> extensions,
            @NotNull String name
    ) {
        DefaultActionGroup templates = new DefaultActionGroup();
        templates.setPopup(true);
        templates.getTemplatePresentation().setText(name);

        extensions
                .map(ConcordionExtension::fileTemplate)
                .map(CreateFromTemplateAction::new)
                .forEach(templates::add);

        return templates;
    }
}
