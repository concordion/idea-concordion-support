package org.concordion.plugin.idea.menu;

import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.*;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.testFramework.MapDataContext;
import com.intellij.ui.EditorTextField;
import org.concordion.plugin.idea.ConcordionExtension;
import org.concordion.plugin.idea.fixtures.ConcordionTestFixture;
import org.concordion.plugin.idea.specifications.ConcordionSpecification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.fixtures;
import static org.concordion.plugin.idea.menu.ConcordionDestinationFolderComboBox.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specifications;

public class ConcordionNewSpecAndFixtureDialog extends DialogWrapper {

    @NotNull private final Project project;
    @NotNull private final ConcordionSpecAndFixtureCreationParameters parameters;

    /* Custom initialization via createUIComponents() */ @NotNull private JPanel panel;
    /* Custom initialization via createUIComponents() */ @NotNull private EditorTextField specName;
    /* Custom initialization via createUIComponents() */ @NotNull private PackageNameReferenceEditorCombo specPackage;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderComboBox specDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderComboBox fixtureDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionSpecification> specTypeSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionTestFixture> fixtureTypeSelector;

    public ConcordionNewSpecAndFixtureDialog(@NotNull Project project, @NotNull ConcordionSpecAndFixtureCreationParameters parameters) {
        super(project, true);
        this.project = project;
        this.parameters = parameters;
        setTitle("Set up Concordion test and fixture");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    private void createUIComponents() {
        specName = new EditorTextField(parameters.initialName);
        specPackage = new PackageNameReferenceEditorCombo(parameters.initialPackage, project, "concordion.new.package", "Concordion spec package");
        specDestinationSelector = resourcesSelector(project, specPackage);
        fixtureDestinationSelector = sourcesSelector(project, specPackage);
        specTypeSelector = new ConcordionExtensionComboBox<>(project, "concordion.new.specification.type", specifications());
        fixtureTypeSelector = new ConcordionExtensionComboBox<>(project, "concordion.new.fixture.type", fixtures());

        if (parameters.nameAndPackagePredefined()) {
            specName.setEnabled(false);
            specPackage.setEnabled(false);
        }
        if (parameters.spec != null) {
            specDestinationSelector.forceSelect(parameters.spec.getContainingDirectory());
            specTypeSelector.forceSelect(parameters.spec.getLanguage());
        }
        if (parameters.fixture != null) {
            fixtureDestinationSelector.forceSelect(parameters.fixture.getContainingFile().getContainingDirectory());
            fixtureTypeSelector.forceSelect(parameters.fixture.getContainingFile().getLanguage());
        }

        specName.requestFocus();
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

        if (parameters.fixture == null) {
            createConcordionFile(fixtureTypeSelector.select(), name, fixtureDestinationSelector.select());
        }
        if (parameters.spec == null) {
            createConcordionFile(specTypeSelector.select(), name, specDestinationSelector.select());
        }

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
        }.actionPerformed(createEvent());
    }

    @NotNull
    private static AnActionEvent createEvent() {
        MapDataContext context = new MapDataContext();
        context.put(LangDataKeys.IDE_VIEW, new EmptyIdeView());
        return AnActionEvent.createFromDataContext(ActionPlaces.UNKNOWN, null, context);
    }

    private static class EmptyIdeView implements IdeView {
        @Override
        public void selectElement(PsiElement psiElement) {

        }

        @NotNull
        @Override
        public PsiDirectory[] getDirectories() {
            return new PsiDirectory[0];
        }

        @Nullable
        @Override
        public PsiDirectory getOrChooseDirectory() {
            return null;
        }
    }
}
