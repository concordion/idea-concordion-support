package org.concordion.plugin.idea.menu;

import com.intellij.ide.util.DirectoryChooser;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import static org.concordion.plugin.idea.SourceRootTypeUtils.*;

public class ConcordionDestinationFolderChooser extends ComponentWithBrowseButton<EditorTextField> {

    @Nullable private PsiDirectory selected;

    @NotNull private PsiDirectory[] directories;
    @Nullable private PsiDirectory initialDir;

    private boolean firstInitialization = true;

    public ConcordionDestinationFolderChooser(
            @NotNull Project project,
            @NotNull PackageNameReferenceEditorCombo packageSource,
            @Nullable PsiDirectory initial,
            @NotNull Set<? extends JpsModuleSourceRootType<?>> directoriesType
    ) {

        super(createTextField(), null);

        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DirectoryChooser directoryChooser = new DirectoryChooser(project);
                directoryChooser.fillList(directories, initialDir, project, "");
                directoryChooser.showAndGet();
                useDirectory(directoryChooser.getSelectedDirectory());
            }
        });
        packageSource.getChildComponent().addDocumentListener(new DocumentAdapter() {
            @Override
            public void documentChanged(DocumentEvent e) {
                if (firstInitialization) {
                    directories = directories(initial, inAllProject(project, directoriesType));
                    initialDir = initial;
                    firstInitialization = false;
                } else {
                    directories = directories(packageSource.getText(), inAllProject(project, directoriesType));
                    initialDir = lastDir(directories);
                }
                useDirectory(initialDir);
            }
        });
        useDirectory(initial);
    }

    public void forceSelect(@NotNull PsiDirectory containingDirectory) {
        useDirectory(containingDirectory);
        getButton().setEnabled(false);
    }

    @Nullable
    public PsiDirectory select() {
        return selected;
    }

    private void useDirectory(@Nullable PsiDirectory directory) {
        selected = directory;
        getChildComponent().setText(projectDirectoryPath());
    }

    @NotNull
    private String projectDirectoryPath() {
        if (selected == null) {
            return "";
        }
        String directoryUrl = selected.getVirtualFile().getPresentableUrl();
        final VirtualFile baseDir = selected.getProject().getBaseDir();
        if (baseDir != null) {
            final String projectHomeUrl = baseDir.getPresentableUrl();
            if (directoryUrl.startsWith(projectHomeUrl)) {
                return  "..." + directoryUrl.substring(projectHomeUrl.length());
            }
        }
        return  directoryUrl;
    }

    @NotNull
    private static EditorTextField createTextField() {
        EditorTextField field = new EditorTextField();
        field.setEnabled(false);
        return field;
    }
}
