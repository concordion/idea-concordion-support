package org.concordion.plugin.idea.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ConcordionSettingsForm {

    /* Custom initialization via createUIComponents() */ @NotNull private JPanel settingsPanel;
    /* Custom initialization via createUIComponents() */ @NotNull private JComboBox concordionCommandsCompletionType;
    /* Custom initialization via createUIComponents() */ @NotNull private EnumComboBoxModel<ConcordionCommandsCaseType> concordionCommandsCompletionTypeModel;

    @NotNull
    public JComponent settingsPanel() {
        return settingsPanel;
    }

    private void createUIComponents() {
        concordionCommandsCompletionTypeModel = new EnumComboBoxModel<>(ConcordionCommandsCaseType.class);
        concordionCommandsCompletionType = new ComboBox(concordionCommandsCompletionTypeModel);
    }

    public void apply() {
        ConcordionSettings.getInstance().setCommandsCaseType(concordionCommandsCompletionTypeModel.getSelectedItem());
    }

    public void reset() {
        concordionCommandsCompletionTypeModel.setSelectedItem(ConcordionSettings.getInstance().getCommandsCaseType());
    }

    public boolean isModified() {
        return !ConcordionSettings.getInstance().getCommandsCaseType().equals(concordionCommandsCompletionTypeModel.getSelectedItem());
    }
}
