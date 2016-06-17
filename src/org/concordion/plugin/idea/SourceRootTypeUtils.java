package org.concordion.plugin.idea;

import com.intellij.openapi.module.Module;
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
import java.util.stream.Stream;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.stream;

public class SourceRootTypeUtils {

    public static boolean directoryIn(
            @Nullable PsiDirectory directory,
            @NotNull PlaceToSearch search) {
        return newHashSet(directories(directory, search)).contains(directory);
    }

    @NotNull
    public static PsiDirectory[] directories(
            @NotNull String pkg,
            @NotNull PlaceToSearch search) {
        return directories(JavaPsiFacade.getInstance(search.project).findPackage(pkg), search);
    }

    @NotNull
    public static PsiDirectory[] directories(
            @Nullable PsiDirectory dir,
            @NotNull PlaceToSearch search) {
        if (dir == null) {
            return PsiDirectory.EMPTY_ARRAY;
        }
        return directories(JavaDirectoryService.getInstance().getPackage(dir), search);
    }

    @NotNull
    public static PsiDirectory[] directories(
            @Nullable PsiPackage aPackage,
            @NotNull PlaceToSearch search) {
        if (aPackage == null) {
            return PsiDirectory.EMPTY_ARRAY;
        }
        return aPackage.getDirectories(search.searchScope());
    }

    @Nullable
    public static PsiDirectory lastDir(@NotNull PsiDirectory[] directories) {
        return directories.length != 0 ? directories[directories.length - 1] : null;
    }

    @NotNull
    public static PlaceToSearch inAllProject(@NotNull Project project, @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        return new PlaceToSearch(
                project,
                sourceRootTypes,
                stream(ModuleManager.getInstance(project).getModules()).flatMap(module -> sourceFolders(module, sourceRootTypes)).toArray(VirtualFile[]::new)
        );
    }

    @NotNull
    public static PlaceToSearch inSingleModule(@NotNull Module module, @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        return new PlaceToSearch(
                module.getProject(),
                sourceRootTypes,
                sourceFolders(module, sourceRootTypes).toArray(VirtualFile[]::new)
        );
    }

    @NotNull
    private static Stream<VirtualFile> sourceFolders(@NotNull Module module, @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes) {
        return stream(ModuleRootManager.getInstance(module).getContentEntries())
                .flatMap(entry -> entry.getSourceFolders(sourceRootTypes).stream())
                .map(ContentFolder::getFile);
    }

    public static final class PlaceToSearch {

        @NotNull private final Project project;
        @NotNull private final Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes;
        @NotNull private final VirtualFile[] directories;

        public PlaceToSearch(
                @NotNull Project project,
                @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes,
                @NotNull VirtualFile[] directories) {
            this.project = project;
            this.sourceRootTypes = sourceRootTypes;
            this.directories = directories;
        }

        @NotNull
        public GlobalSearchScope searchScope() {
            return GlobalSearchScopes.directoriesScope(
                    project,
                    true,
                    directories
            );
        }
    }
}
