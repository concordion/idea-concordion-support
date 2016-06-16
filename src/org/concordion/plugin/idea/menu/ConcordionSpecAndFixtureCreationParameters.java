package org.concordion.plugin.idea.menu;

import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Optional.ofNullable;
import static org.apache.commons.io.FilenameUtils.removeExtension;

public class ConcordionSpecAndFixtureCreationParameters {

    @NotNull public final String initialName;
    @NotNull public final String initialPackage;
    @Nullable public final PsiFile spec;
    @Nullable public final PsiClass fixture;

    private ConcordionSpecAndFixtureCreationParameters(
            @NotNull String initialName,
            @NotNull String initialPackage,
            @Nullable PsiFile spec,
            @Nullable PsiClass fixture
    ) {
        this.initialName = initialName;
        this.initialPackage = initialPackage;
        this.spec = spec;
        this.fixture = fixture;
    }

    @NotNull
    public static ConcordionSpecAndFixtureCreationParameters fromScratch(@NotNull AnActionEvent event) {
        return new ConcordionSpecAndFixtureCreationParameters(
                "Spec",
                packageFromEvent(event),
                null,
                null
        );
    }

    @NotNull
    public static ConcordionSpecAndFixtureCreationParameters fromExistingSpec(@NotNull PsiFile spec) {
        return new ConcordionSpecAndFixtureCreationParameters(
                removeExtension(spec.getName()),
                packageFromDirectory(spec.getContainingDirectory()),
                spec,
                null
        );
    }

    @NotNull
    public static ConcordionSpecAndFixtureCreationParameters fromExistingFixture(@NotNull PsiClass fixture) {
        return new ConcordionSpecAndFixtureCreationParameters(
                fixture.getName(),
                packageFromDirectory(fixture.getContainingFile().getContainingDirectory()),
                null,
                fixture
        );
    }

    public boolean nameAndPackagePredefined() {
        return spec != null || fixture != null;
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
