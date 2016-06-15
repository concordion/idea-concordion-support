package org.concordion.plugin.idea.menu;

import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.*;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.ui.EditorTextField;
import org.concordion.plugin.idea.ConcordionExtension;
import org.concordion.plugin.idea.fixtures.ConcordionTestFixture;
import org.concordion.plugin.idea.specifications.ConcordionSpecification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static java.util.Optional.ofNullable;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.fixtures;
import static org.concordion.plugin.idea.menu.ConcordionDestinationFolderComboBox.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specifications;

public class ConcordionNewSpecAndFixtureDialog extends DialogWrapper {

    @NotNull private final AnActionEvent event;
    @NotNull private final Project project;
    @Nullable private final String initialPackage;
    /* Custom initialization via createUIComponents() */ @NotNull private JPanel panel;
    /* Custom initialization via createUIComponents() */ @NotNull private EditorTextField specName;
    /* Custom initialization via createUIComponents() */ @NotNull private PackageNameReferenceEditorCombo specPackage;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderComboBox specDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderComboBox fixtureDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionSpecification> specTypeSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionTestFixture> fixtureTypeSelector;

    public static boolean show(@NotNull AnActionEvent event) {
        if (event.getProject() == null) {
            return false;
        }
        return new ConcordionNewSpecAndFixtureDialog(
                event, event.getProject(), packageFromEvent(event)
        ).showAndGet();
    }

    public ConcordionNewSpecAndFixtureDialog(@NotNull AnActionEvent event, @NotNull Project project, @Nullable String initialPackage) {
        super(project, true);
        this.event = event;
        this.project = project;
        this.initialPackage = initialPackage;
        setTitle("Create Spec and Fixture");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    private void createUIComponents() {
        specName = new EditorTextField("Spec");
        specPackage = new PackageNameReferenceEditorCombo(initialPackage, project, "concordion.new.package", "Concordion spec package");
        specDestinationSelector = resourcesSelector(project, specPackage);
        fixtureDestinationSelector = sourcesSelector(project, specPackage);
        specTypeSelector = new ConcordionExtensionComboBox<>(project, "concordion.new.specification.type", specifications());
        fixtureTypeSelector = new ConcordionExtensionComboBox<>(project, "concordion.new.fixture.type", fixtures());
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (specName.getText().isEmpty()) {
            return new ValidationInfo("Spec name must be set", specName);
        }
        return super.doValidate();
    }

    @Override
    protected void doOKAction() {
        String name = specName.getText();

        createConcordionFile(fixtureTypeSelector.select(), name, fixtureDestinationSelector.select());
        createConcordionFile(specTypeSelector.select(), name, specDestinationSelector.select());

        super.doOKAction();
    }

    private void createConcordionFile(@NotNull ConcordionExtension ext, @NotNull String name, @Nullable PsiDirectory destination) {
        new CreateFromTemplateAction(ext.fileTemplate()) {
            @Nullable
            @Override
            protected PsiDirectory getTargetDirectory(DataContext dataContext, IdeView view) {
                return destination;
            }

            @Nullable
            @Override
            public AttributesDefaults getAttributesDefaults(DataContext dataContext) {
                return new AttributesDefaults(name).withFixedName(true);
            }
        }.actionPerformed(event);
    }

    @Nullable
    private static String packageFromEvent(@NotNull AnActionEvent event) {
        return ofNullable(LangDataKeys.IDE_VIEW.getData(event.getDataContext()))
                .map(DirectoryChooserUtil::getOrChooseDirectory)
                .map(directory -> JavaDirectoryService.getInstance().getPackage(directory))
                .map(PsiPackage::getQualifiedName)
                .orElse(null);
    }
}
