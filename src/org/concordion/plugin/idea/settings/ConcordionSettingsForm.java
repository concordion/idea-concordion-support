package org.concordion.plugin.idea.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ConcordionSettingsForm {

    /* Custom initialization via createUIComponents() */ @NotNull private JPanel settingsPanel;
    /* Custom initialization via createUIComponents() */ @NotNull private JComboBox concordionCommandsCompletionType;
    /* Custom initialization via createUIComponents() */ @NotNull private JComboBox renamePairedConcordionFile;
    /* Custom initialization via createUIComponents() */ @NotNull private JComboBox deletePairedConcordionFile;
    /* Custom initialization via createUIComponents() */ @NotNull private EnumComboBoxModel<ConcordionCommandsCaseType> concordionCommandsCompletionTypeModel;
    /* Custom initialization via createUIComponents() */ @NotNull private EnumComboBoxModel<ConcordionFilesRefactoring> renamePairedConcordionFileModel;
    /* Custom initialization via createUIComponents() */ @NotNull private EnumComboBoxModel<ConcordionFilesRefactoring> deletePairedConcordionFileModel;

    @NotNull
    public JComponent settingsPanel() {
        return settingsPanel;
    }

    private void createUIComponents() {
        concordionCommandsCompletionTypeModel = new EnumComboBoxModel<>(ConcordionCommandsCaseType.class);
        renamePairedConcordionFileModel = new EnumComboBoxModel<>(ConcordionFilesRefactoring.class);
        deletePairedConcordionFileModel = new EnumComboBoxModel<>(ConcordionFilesRefactoring.class);

        concordionCommandsCompletionType = new ComboBox(concordionCommandsCompletionTypeModel);
        renamePairedConcordionFile = new ComboBox(renamePairedConcordionFileModel);
        deletePairedConcordionFile = new ComboBox(deletePairedConcordionFileModel);
    }

    public void apply() {
        ConcordionSettings settings = ConcordionSettings.getInstance();

        settings.setCommandsCaseType(concordionCommandsCompletionTypeModel.getSelectedItem());
        settings.setRenamePairs(renamePairedConcordionFileModel.getSelectedItem());
        settings.setDeletePairs(deletePairedConcordionFileModel.getSelectedItem());
    }

    public void reset() {
        ConcordionSettings settings = ConcordionSettings.getInstance();

        concordionCommandsCompletionTypeModel.setSelectedItem(settings.getCommandsCaseType());
        renamePairedConcordionFileModel.setSelectedItem(settings.getRenamePairs());
        deletePairedConcordionFileModel.setSelectedItem(settings.getDeletePairs());
    }

    public boolean isModified() {
        ConcordionSettings settings = ConcordionSettings.getInstance();

        return !settings.getCommandsCaseType().equals(concordionCommandsCompletionTypeModel.getSelectedItem())
                || !settings.getRenamePairs().equals(renamePairedConcordionFileModel.getSelectedItem())
                || !settings.getDeletePairs().equals(deletePairedConcordionFileModel.getSelectedItem());
    }
}
