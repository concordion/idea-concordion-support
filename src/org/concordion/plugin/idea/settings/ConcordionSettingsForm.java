package org.concordion.plugin.idea.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ConcordionSettingsForm {

    @NotNull private JPanel settingsPanel;
    @NotNull private JComboBox concordionCommandsCompletionType;
    @NotNull private EnumComboBoxModel<ConcordionCommandsCaseType> concordionCommandsCompletionTypeModel;

    @NotNull
    public JComponent settingsPanel() {
        return settingsPanel;
    }

    private void createUIComponents() {
        concordionCommandsCompletionTypeModel = new EnumComboBoxModel<>(ConcordionCommandsCaseType.class);
        concordionCommandsCompletionType = new ComboBox(concordionCommandsCompletionTypeModel);
    }

    public void setSettings(@NotNull ConcordionSettingsState settings) {
        concordionCommandsCompletionTypeModel.setSelectedItem(settings.getCommandsCaseType());
    }

    @NotNull
    public ConcordionSettingsState createSettings() {
        ConcordionSettingsState settings = new ConcordionSettingsState();
        settings.setCommandsCaseType(concordionCommandsCompletionTypeModel.getSelectedItem());
        return settings;
    }
}
