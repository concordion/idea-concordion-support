package org.concordion.plugin.idea;

import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentFolder;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.stream;

public class SourceRootTypeUtils {

    @NotNull
    public static GlobalSearchScope sourceTypeSearchScope(@NotNull Project project, @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        return GlobalSearchScopes.directoriesScope(
                project,
                true,
                stream(ModuleManager.getInstance(project).getModules())
                        .flatMap(module -> stream(ModuleRootManager.getInstance(module).getContentEntries()))
                        .flatMap(entry -> entry.getSourceFolders(sourceRootTypes).stream())
                        .map(ContentFolder::getFile)
                        .toArray(VirtualFile[]::new)
        );
    }

    public static boolean checkDirectoryBelongsToRootType(
            @Nullable PsiDirectory directory,
            @NotNull Project project,
            @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        return newHashSet(directoriesOfType(directory, project, sourceRootTypes)).contains(directory);
    }

    @NotNull
    public static PsiDirectory[] directoriesOfType(
            @NotNull String pkg,
            @NotNull Project project,
            @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        return directoriesOfType(JavaPsiFacade.getInstance(project).findPackage(pkg), project, sourceRootTypes);
    }

    @NotNull
    public static PsiDirectory[] directoriesOfType(
            @Nullable PsiDirectory dir,
            @NotNull Project project,
            @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        if (dir == null) {
            return PsiDirectory.EMPTY_ARRAY;
        }
        return directoriesOfType(JavaDirectoryService.getInstance().getPackage(dir), project, sourceRootTypes);
    }

    @NotNull
    public static PsiDirectory[] directoriesOfType(
            @Nullable PsiPackage aPackage,
            @NotNull Project project,
            @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        if (aPackage == null) {
            return PsiDirectory.EMPTY_ARRAY;
        }
        return aPackage.getDirectories(sourceTypeSearchScope(project, sourceRootTypes));
    }

    @Nullable
    public static PsiDirectory lastDir(@NotNull PsiDirectory[] directories) {
        return directories.length != 0 ? directories[directories.length - 1] : null;
    }
}
