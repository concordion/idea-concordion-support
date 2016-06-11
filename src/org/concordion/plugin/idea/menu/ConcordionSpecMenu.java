package org.concordion.plugin.idea.menu;

import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.ide.fileTemplates.impl.BundledFileTemplate;
import com.intellij.ide.fileTemplates.impl.DefaultTemplate;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiPackage;
import org.concordion.plugin.idea.ConcordionExtension;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.fixtures;
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

    @NotNull
    private static AnAction specAndFixtureDialog() {
        return new AnAction("Spec and fixture") {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
//                new ConcordionNewSpecAndFixtureDialog(anActionEvent.getProject(), packageFromEvent(anActionEvent)).showAndGet();
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
                .map(ConcordionExtension::template)
                .map(ConcordionSpecMenu::createFromTemplateAction)
                .forEach(templates::add);

        return templates;
    }

    @NotNull
    private static CreateFromTemplateAction createFromTemplateAction(@NotNull DefaultTemplate template) {
        return new CreateFromTemplateAction(new BundledFileTemplate(template, true));
    }

    @Nullable
    private static String packageFromEvent(AnActionEvent event) {
        return ofNullable(LangDataKeys.IDE_VIEW.getData(event.getDataContext()))
                .map(DirectoryChooserUtil::getOrChooseDirectory)
                .map(directory -> JavaDirectoryService.getInstance().getPackage(directory))
                .map(PsiPackage::getQualifiedName)
                .orElse(null);
    }
}
