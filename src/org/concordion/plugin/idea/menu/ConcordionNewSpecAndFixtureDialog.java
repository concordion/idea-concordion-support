package org.concordion.plugin.idea.menu;

import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
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
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

import javax.swing.*;

import static org.concordion.plugin.idea.ConcordionNavigationService.*;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.fixtures;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specifications;

public class ConcordionNewSpecAndFixtureDialog extends DialogWrapper {

    @NotNull private final Project project;
    @NotNull private final ConcordionSpecAndFixtureCreationParameters parameters;

    /* Custom initialization via createUIComponents() */ @NotNull private JPanel panel;
    /* Custom initialization via createUIComponents() */ @NotNull private EditorTextField specName;
    /* Custom initialization via createUIComponents() */ @NotNull private PackageNameReferenceEditorCombo specPackage;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderChooser specDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderChooser fixtureDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionSpecification> specTypeSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionTestFixture> fixtureTypeSelector;

    public ConcordionNewSpecAndFixtureDialog(@NotNull Project project, @NotNull ConcordionSpecAndFixtureCreationParameters parameters) {
        super(project, true);
        this.project = project;
        this.parameters = parameters;
        setTitle("Set up Concordion specification and fixture");
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
        specDestinationSelector = new ConcordionDestinationFolderChooser(project, specPackage, parameters.specDirectory, JavaModuleSourceRootTypes.RESOURCES);
        fixtureDestinationSelector = new ConcordionDestinationFolderChooser(project, specPackage, parameters.fixtureDirectory, JavaModuleSourceRootTypes.SOURCES);
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
        String specNameText = specName.getText();
        if (specNameText.isEmpty()) {
            return new ValidationInfo("Spec name must be set", specName);
        }
        if (!PsiNameHelper.getInstance(project).isQualifiedName(specNameText)) {
            return new ValidationInfo("This is not a valid specification name", specName);
        }
        if (specNameText.endsWith(OPTIONAL_TEST_SUFFIX)
                || specNameText.endsWith(OPTIONAL_FIXTURE_SUFFIX)) {
            return new ValidationInfo("It is illegal to use Test and Fixture suffixes for concordion files", specName);
        }
        if (specDestinationSelector.select() == null) {
            return new ValidationInfo("Spec destination must be selected.<br/> Empty directory list may indicate <li>no resource directories configured in project</li><li>selected package is not present in any of resource directories</li>", specDestinationSelector);
        }
        if (fixtureDestinationSelector.select() == null) {
            return new ValidationInfo("Fixture destination must be selected.<br/> Empty directory list may indicate <li>no source directories configured in project</li><li>selected package is not present in any of source directories</li>", fixtureDestinationSelector);
        }
        return super.doValidate();
    }

    public void showDialog() {
        if (ApplicationManager.getApplication().isUnitTestMode() || showAndGet()) {
            createConcordionFiles();
        }
    }

    private void createConcordionFiles() {
        String name = specName.getText();

        if (parameters.fixture == null) {
            createConcordionFile(fixtureTypeSelector.select(), name, fixtureDestinationSelector.select());
        }
        if (parameters.spec == null) {
            createConcordionFile(specTypeSelector.select(), name, specDestinationSelector.select());
        }
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
