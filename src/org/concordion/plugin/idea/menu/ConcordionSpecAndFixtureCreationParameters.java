package org.concordion.plugin.idea.menu;

import com.intellij.ide.IdeView;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.concordion.plugin.idea.SourceRootTypeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.concordion.plugin.idea.SourceRootTypeUtils.*;

public class ConcordionSpecAndFixtureCreationParameters {

    @NotNull public final String initialName;
    @NotNull public final String initialPackage;
    @Nullable public final PsiFile spec;
    @Nullable public final PsiClass fixture;
    @Nullable public final PsiDirectory specDirectory;
    @Nullable public final PsiDirectory fixtureDirectory;

    private ConcordionSpecAndFixtureCreationParameters(
            @NotNull String initialName,
            @NotNull String initialPackage,
            @Nullable PsiFile spec,
            @Nullable PsiClass fixture,
            @Nullable PsiDirectory specDirectory,
            @Nullable PsiDirectory fixtureDirectory
    ) {
        this.initialName = initialName;
        this.initialPackage = initialPackage;
        this.spec = spec;
        this.fixture = fixture;
        this.specDirectory = specDirectory;
        this.fixtureDirectory = fixtureDirectory;
    }

    @NotNull
    public static ConcordionSpecAndFixtureCreationParameters fromScratch(@NotNull AnActionEvent event) {

        Project project = event.getProject();
        PsiDirectory selectedDirectory = dirFromEvent(event);

        return new ConcordionSpecAndFixtureCreationParameters(
                "",
                packageFromEvent(event),
                null,
                null,
                smartFindDirectory(selectedDirectory, project, JavaModuleSourceRootTypes.RESOURCES),
                smartFindDirectory(selectedDirectory, project, JavaModuleSourceRootTypes.SOURCES)
        );
    }

    @NotNull
    public static ConcordionSpecAndFixtureCreationParameters fromExistingSpec(@NotNull PsiFile spec) {

        PsiDirectory specDirectory = spec.getContainingDirectory();

        return new ConcordionSpecAndFixtureCreationParameters(
                removeExtension(spec.getName()),
                packageFromDirectory(specDirectory),
                spec,
                null,
                specDirectory,
                smartFindDirectory(specDirectory, spec.getProject(), JavaModuleSourceRootTypes.SOURCES)
        );
    }

    @NotNull
    public static ConcordionSpecAndFixtureCreationParameters fromExistingFixture(@NotNull PsiClass fixture) {

        PsiDirectory fixtureDirectory = fixture.getContainingFile().getContainingDirectory();

        return new ConcordionSpecAndFixtureCreationParameters(
                fixture.getName(),
                packageFromDirectory(fixtureDirectory),
                null,
                fixture,
                smartFindDirectory(fixtureDirectory, fixture.getProject(), JavaModuleSourceRootTypes.RESOURCES),
                fixtureDirectory
        );
    }

    public boolean nameAndPackagePredefined() {
        return spec != null || fixture != null;
    }

    //TODO suggest with respect to test/prod
    @Nullable
    private static PsiDirectory smartFindDirectory(
            @Nullable PsiDirectory directory,
            @NotNull Project project,
            @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes
    ) {
        return checkDirectoryBelongsToRootType(directory, project, sourceRootTypes) ? directory : lastDir(directoriesOfType(directory, project, sourceRootTypes));
    }

    @Nullable
    private static PsiDirectory dirFromEvent(@NotNull AnActionEvent event) {
        return ofNullable(LangDataKeys.IDE_VIEW.getData(event.getDataContext()))
                .map(IdeView::getDirectories)
                .map(SourceRootTypeUtils::lastDir)
                .orElse(null);
    }

    @NotNull
    private static String packageFromEvent(@NotNull AnActionEvent event) {
        return ofNullable(LangDataKeys.IDE_VIEW.getData(event.getDataContext()))
                .map(DirectoryChooserUtil::getOrChooseDirectory)
                .map(directory -> JavaDirectoryService.getInstance().getPackage(directory))
                .map(PsiPackage::getQualifiedName)
                .orElse("");
    }

    @NotNull
    private static String packageFromDirectory(@NotNull PsiDirectory directory) {
        return ofNullable(JavaDirectoryService.getInstance().getPackage(directory))
                .map(PsiPackage::getQualifiedName)
                .orElse("");
    }
}
