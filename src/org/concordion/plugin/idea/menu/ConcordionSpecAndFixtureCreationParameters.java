package org.concordion.plugin.idea.menu;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.intellij.ide.IdeView;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.concordion.plugin.idea.SourceRootTypeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.concordion.plugin.idea.ConcordionPsiUtils.removeExtension;
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
                smartFindDirectory(selectedDirectory, JavaModuleSourceRootTypes.RESOURCES),
                smartFindDirectory(selectedDirectory, JavaModuleSourceRootTypes.SOURCES)
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
                smartFindDirectory(specDirectory, JavaModuleSourceRootTypes.SOURCES)
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
                smartFindDirectory(fixtureDirectory, JavaModuleSourceRootTypes.RESOURCES),
                fixtureDirectory
        );
    }

    public boolean nameAndPackagePredefined() {
        return spec != null || fixture != null;
    }

    @Nullable
    private static PsiDirectory smartFindDirectory(
            @Nullable PsiDirectory directory,
            @NotNull Set<? extends JpsModuleSourceRootType<?>> sourceRootTypes
    ) {
        if (directory == null) {
            return null;
        }
        Module module = ModuleUtilCore.findModuleForPsiElement(directory);
        if (module == null) {
            return null;
        }
        if (directoryIn(directory, inSingleModule(module, sourceRootTypes))) {
            return directory;
        }
        Map<JpsModuleSourceRootType<?>, JpsModuleSourceRootType<?>> mySuggestions = new HashMap<>();
        mySuggestions.putAll(SUGGESTIONS);
        sourceRootTypes.forEach(mySuggestions::remove);

        return mySuggestions.entrySet().stream()
                .filter(suggestion -> directoryIn(directory, inSingleModule(module, ImmutableSet.of(suggestion.getKey()))))
                .map(suggestion -> lastDir(directories(directory, inSingleModule(module, ImmutableSet.of(suggestion.getValue())))))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    private static final Map<JpsModuleSourceRootType<?>, JpsModuleSourceRootType<?>> SUGGESTIONS = ImmutableMap.of(
            JavaSourceRootType.SOURCE,  JavaResourceRootType.RESOURCE,
            JavaSourceRootType.TEST_SOURCE, JavaResourceRootType.TEST_RESOURCE,
            JavaResourceRootType.RESOURCE, JavaSourceRootType.SOURCE,
            JavaResourceRootType.TEST_RESOURCE, JavaSourceRootType.TEST_SOURCE
    );

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
