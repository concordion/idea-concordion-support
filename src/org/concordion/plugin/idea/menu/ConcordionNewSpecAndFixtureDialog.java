package org.concordion.plugin.idea.menu;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiDirectory;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.ui.EditorTextField;
import org.concordion.plugin.idea.fixtures.ConcordionTestFixture;
import org.concordion.plugin.idea.specifications.ConcordionSpecification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.fixtures;
import static org.concordion.plugin.idea.menu.ConcordionDestinationFolderComboBox.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specifications;

public class ConcordionNewSpecAndFixtureDialog extends DialogWrapper {

    @NotNull private Project project;
    @Nullable private String initialPackage;
    /* Custom initialization via createUIComponents() */ @NotNull private JPanel panel;
    /* Custom initialization via createUIComponents() */ @NotNull private EditorTextField specName;
    /* Custom initialization via createUIComponents() */ @NotNull private PackageNameReferenceEditorCombo specPackage;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderComboBox specDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionDestinationFolderComboBox fixtureDestinationSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionSpecification> specTypeSelector;
    /* Custom initialization via createUIComponents() */ @NotNull private ConcordionExtensionComboBox<ConcordionTestFixture> fixtureTypeSelector;

    public ConcordionNewSpecAndFixtureDialog(@NotNull Project project, @Nullable String initialPackage) {
        super(project, true);
        this.project = project;
        this.initialPackage = initialPackage;
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

    @Override
    protected void doOKAction() {
        PsiDirectory specDestination = specDestinationSelector.select();
        PsiDirectory fixtureDestination = fixtureDestinationSelector.select();
        ConcordionSpecification specType = specTypeSelector.select();
        ConcordionTestFixture fixtureType = fixtureTypeSelector.select();

        if (specDestination != null && fixtureDestination != null) {
            //TODO perform creation
        }

        super.doOKAction();
    }
}
