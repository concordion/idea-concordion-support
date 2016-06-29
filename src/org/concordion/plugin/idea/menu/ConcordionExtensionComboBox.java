package org.concordion.plugin.idea.menu;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.ui.RecentsManager;
import org.concordion.plugin.idea.ConcordionExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.stream.Stream;

public class ConcordionExtensionComboBox<T extends ConcordionExtension> extends ComboBox {

    @NotNull private final Project project;
    @NotNull private final String recencyKey;

    @NotNull private final DefaultComboBoxModel<T> model;

    public ConcordionExtensionComboBox(@NotNull Project project, @NotNull String recencyKey, @NotNull Stream<T> extensions) {
        this.project = project;
        this.recencyKey = recencyKey;
        setModel(model = new DefaultComboBoxModel<T>());
        setRenderer(new ConcordionExtensionColoredListCellRendererWrapper());
        setOptionsFrom(extensions);
    }

    @NotNull
    public T select() {
        T extension = (T) model.getSelectedItem();
        RecentsManager.getInstance(project).registerRecentEntry(recencyKey, extension.language().getDisplayName());
        return extension;
    }

    public void forceSelect(@NotNull Language language) {
        for (int i = 0; i < getModel().getSize(); i++) {
            T element = model.getElementAt(i);
            if (element.language().equals(language)) {
                model.setSelectedItem(element);
            }
        }
        setEnabled(false);
    }

    @Nullable
    private String lastUsed() {
        List<String> recentEntries = RecentsManager.getInstance(project).getRecentEntries(recencyKey);
        return recentEntries != null && recentEntries.size() > 0 ? recentEntries.get(0) : null;
    }

    private void setOptionsFrom(@NotNull Stream<T> extensions) {
        String lastUsed = lastUsed();

        extensions.forEach(ext -> {
            model.addElement(ext);
            if (ext.language().getDisplayName().equals(lastUsed)) {
                setSelectedItem(ext);
            }
        });
    }

    @Nullable
    private static Icon computeIcon(@NotNull ConcordionExtension extension) {
        return FileTypeManager.getInstance().getFileTypeByExtension(extension.fileExtensions().iterator().next()).getIcon();
    }

    private class ConcordionExtensionColoredListCellRendererWrapper extends ColoredListCellRendererWrapper<T> {
        @Override
        protected void doCustomize(JList list, @Nullable T extension, int index, boolean selected, boolean hasFocus) {
            if (extension != null) {
                setIcon(computeIcon(extension));
                append(extension.language().getDisplayName());
            }
        }
    }
}
