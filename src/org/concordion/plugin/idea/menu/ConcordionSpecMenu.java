package org.concordion.plugin.idea.menu;

import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.ide.fileTemplates.impl.BundledFileTemplate;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiPackage;
import org.concordion.plugin.idea.ConcordionExtension;
import org.concordion.plugin.idea.fixtures.ConcordionTestFixture;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.specifications.ConcordionSpecification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Optional.ofNullable;

public class ConcordionSpecMenu extends DefaultActionGroup {

    public ConcordionSpecMenu() {
        setPopup(true);
        Presentation presentation = getTemplatePresentation();
        presentation.setText("Concordion");
        presentation.setIcon(ConcordionIcons.ICON);

        add(specAndFixtureDialog());
        add(createTemplatesMenuFor(ConcordionTestFixture.EP_NAME, "Fixture"));
        add(createTemplatesMenuFor(ConcordionSpecification.EP_NAME, "Specification"));
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
            @NotNull ExtensionPointName<? extends ConcordionExtension> extension,
            @NotNull String name
    ) {
        DefaultActionGroup templates = new DefaultActionGroup();
        templates.setPopup(true);
        templates.getTemplatePresentation().setText(name);

        for (ConcordionExtension ext : extension.getExtensions()) {
            if (ext.template() != null) {
                templates.add(new CreateFromTemplateAction(new BundledFileTemplate(ext.template(), true)));
            }
        }

        return templates;
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
