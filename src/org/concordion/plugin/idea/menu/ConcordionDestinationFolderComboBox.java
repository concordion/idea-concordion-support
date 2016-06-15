package org.concordion.plugin.idea.menu;

import com.google.common.collect.ImmutableMap;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentFolder;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import javax.swing.*;
import java.util.Collection;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConcordionDestinationFolderComboBox extends ComboBox {

    @NotNull private static final Map<JpsModuleSourceRootType, Icon> ICONS = ImmutableMap.of(
            JavaSourceRootType.SOURCE, AllIcons.Modules.SourceRoot,
            JavaResourceRootType.RESOURCE, AllIcons.Modules.ResourcesRoot,
            JavaSourceRootType.TEST_SOURCE, AllIcons.Modules.TestRoot,
            JavaResourceRootType.TEST_RESOURCE, AllIcons.Modules.TestResourcesRoot
    );

    @NotNull private final Project project;
    @NotNull private final PackageNameReferenceEditorCombo packageSelector;
    @NotNull private final JpsModuleSourceRootType sourceRootType;
    @NotNull private final JpsModuleSourceRootType testRootType;

    @NotNull private final DefaultComboBoxModel<ConcordionDirectory> myModel;

    @NotNull
    public static ConcordionDestinationFolderComboBox sourcesSelector(@NotNull Project project, @NotNull PackageNameReferenceEditorCombo packageSelector) {
        return new ConcordionDestinationFolderComboBox(project, packageSelector, JavaSourceRootType.SOURCE, JavaSourceRootType.TEST_SOURCE);
    }

    @NotNull
    public static ConcordionDestinationFolderComboBox resourcesSelector(@NotNull Project project, @NotNull PackageNameReferenceEditorCombo packageSelector) {
        return new ConcordionDestinationFolderComboBox(project, packageSelector, JavaResourceRootType.RESOURCE, JavaResourceRootType.TEST_RESOURCE);
    }

    @Nullable
    public PsiDirectory select() {
        ConcordionDirectory item = (ConcordionDirectory) myModel.getSelectedItem();
        return item != null ? item.directory : null;
    }

    private ConcordionDestinationFolderComboBox(
            @NotNull Project project,
            @NotNull PackageNameReferenceEditorCombo packageSelector,
            @NotNull JpsModuleSourceRootType sourceRootType,
            @NotNull JpsModuleSourceRootType testRootType
    ) {
        this.project = project;
        this.packageSelector = packageSelector;
        this.sourceRootType = sourceRootType;
        this.testRootType = testRootType;

        setModel(myModel = new DefaultComboBoxModel<>());
        setRenderer(new ConcordionExtensionColoredListCellRendererWrapper());
        packageSelector.getChildComponent().addDocumentListener(new DocumentAdapter() {
            @Override
            public void documentChanged(DocumentEvent e) {
                setOptionsFromPackageSelector();
            }
        });
    }

    private void setOptionsFromPackageSelector() {
        myModel.removeAllElements();
        PsiPackage aPackage = JavaPsiFacade.getInstance(project).findPackage(packageSelector.getText());
        if (aPackage == null) {
            return;
        }

        PsiDirectory[] directories = aPackage.getDirectories();

        addSourceOptions(testRootType, directories);
        addSourceOptions(sourceRootType, directories);
    }


    private void addSourceOptions(@NotNull JpsModuleSourceRootType rootType, @NotNull PsiDirectory[] directories) {
        for (ModuleAndDirectories moduleAndDirectory : collectRoots(rootType)) {
            for (PsiDirectory directory : directories) {
                VirtualFile moduleDirectory = moduleAndDirectory.moduleDirectoryFor(directory);
                if (moduleDirectory != null) {
                    myModel.addElement(new ConcordionDirectory(
                            directory,
                            ICONS.get(rootType),
                            moduleDirectory.getName(),
                            moduleAndDirectory.module.getName()
                    ));
                }
            }
        }
    }

    @NotNull
    private Collection<ModuleAndDirectories> collectRoots(@NotNull JpsModuleSourceRootType source) {
        return stream(ModuleManager.getInstance(project).getModules())
                .map(module -> createModuleAndDirectories(module, source))
                .collect(toList());
    }

    @NotNull
    private ModuleAndDirectories createModuleAndDirectories(@NotNull Module module, @NotNull JpsModuleSourceRootType source) {
        return new ModuleAndDirectories(
                module,
                stream(ModuleRootManager.getInstance(module).getContentEntries())
                        .flatMap(entry -> entry.getSourceFolders(source).stream())
                        .map(ContentFolder::getFile)
                        .collect(toList())
        );
    }

    private class ConcordionExtensionColoredListCellRendererWrapper extends ColoredListCellRendererWrapper<ConcordionDirectory> {
        @Override
        protected void doCustomize(JList list, @Nullable ConcordionDirectory directory, int index, boolean selected, boolean hasFocus) {
            if (directory != null) {
                setIcon(directory.icon);
                append(directory.name);
                append(" [" + directory.module + "]", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
            }
        }
    }

    private static final class ModuleAndDirectories {
        @NotNull private final Module module;
        @NotNull private final Collection<VirtualFile> directories;

        public ModuleAndDirectories(@NotNull Module module, @NotNull Collection<VirtualFile> directories) {
            this.module = module;
            this.directories = directories;
        }

        @Nullable
        public VirtualFile moduleDirectoryFor(@NotNull PsiDirectory child) {
            String path = child.getVirtualFile().getPath();
            for (VirtualFile directory : directories) {
                if (path.startsWith(directory.getPath())) {
                    return directory;
                }
            }
            return null;
        }
    }

    private static final class ConcordionDirectory {
        @NotNull private final PsiDirectory directory;
        @NotNull private final Icon icon;
        @NotNull private final String name;
        @NotNull private final String module;

        public ConcordionDirectory(@NotNull PsiDirectory directory, @NotNull Icon icon, @NotNull String name, @NotNull String module) {
            this.directory = directory;
            this.icon = icon;
            this.name = name;
            this.module = module;
        }
    }
}
