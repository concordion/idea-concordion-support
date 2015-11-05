package org.concordion.plugin.idea;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.util.Arrays.stream;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public class ConcordionNavigationService {

    public static ConcordionNavigationService getInstance(Project project) {
        return ServiceManager.getService(project, ConcordionNavigationService.class);
    }

    private final PsiElementCache<PsiFile> cache = new PsiElementCache<>(ConcordionNavigationService::getIdentityKey);

    @Nullable
    public  PsiClass correspondingJavaRunner(@Nullable PsiFile htmlSpec) {
        PsiClass testFixture = PsiTreeUtil.getChildOfType(correspondingJavaFile(htmlSpec), PsiClass.class);
        return isConcordionSpecAndFixture(htmlSpec, testFixture) ? testFixture : null;
    }

    @Nullable
    public  PsiFile correspondingHtmlSpec(@Nullable PsiClass testFixture) {
        if (testFixture == null) {
            return null;
        }
        PsiFile htmlSpec = correspondingHtmlFile(testFixture.getContainingFile());
        return isConcordionSpecAndFixture(htmlSpec, testFixture) ? htmlSpec : null;
    }

    @Nullable
    public  PsiFile correspondingSpecFile(@Nullable PsiFile file) {
        if (file == null) {
            return null;
        }

        return JavaFileType.INSTANCE.equals(file.getFileType())
                ? correspondingHtmlFile(file)
                : correspondingJavaFile(file);
    }

    private  final String OPTIONAL_TEST_SUFFIX = "Test";
    private  final String OPTIONAL_FIXTURE_SUFFIX = "Fixture";

    @Nullable
    private PsiFile correspondingJavaFile(@Nullable PsiFile htmlFile) {
        if (!canBeNavigated(htmlFile)) {
            return null;
        }

        String specName = removeExtension(htmlFile.getName());

        if (!specName.equals(removeOptionalSuffix(specName))) {
            return null;
        }

        return cache.getOrCompute(getIdentityKey(htmlFile), () -> findCorrespondingSpecFile(
                htmlFile.getContainingDirectory(),
                specName + ".java",
                specName + OPTIONAL_TEST_SUFFIX + ".java",
                specName + OPTIONAL_FIXTURE_SUFFIX + ".java"
        ));
    }

    @Nullable
    private PsiFile correspondingHtmlFile(@Nullable PsiFile javaFile) {
        if (!canBeNavigated(javaFile)) {
            return null;
        }

        String specName = removeOptionalSuffix(removeExtension(javaFile.getName()));

        return cache.getOrCompute(getIdentityKey(javaFile), () -> findCorrespondingSpecFile(
                javaFile.getContainingDirectory(),
                specName + ".html"
        ));
    }

    @Nullable
    private PsiFile findCorrespondingSpecFile(@NotNull PsiDirectory oneOfPackageDirs, @NotNull String... names) {
        PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(oneOfPackageDirs);
        if (aPackage == null) {
            return null;
        }

        return stream(aPackage.getDirectories())
                .map(directory -> findFirstFile(directory, names))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    @Nullable
    private PsiFile findFirstFile(@NotNull PsiDirectory directory, @NotNull String[] names) {
        return stream(names)
                .map(directory::findFile)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    private boolean canBeNavigated(@Nullable PsiFile file) {
        return file != null && file.getContainingDirectory() != null;
    }

    private boolean isConcordionSpecAndFixture(@Nullable PsiFile spec, @Nullable PsiClass fixture) {
        return (spec != null && isConcordionHtmlSpec(spec))
                || (fixture != null && isConcordionFixture(fixture));
    }

    @NotNull
    private  String removeExtension(@NotNull String fileName) {
        return fileName.substring(0, fileName.indexOf('.'));
    }

    @NotNull
    private String removeSuffix(@NotNull String text, @NotNull String suffix) {
        return text.substring(0, text.length() - suffix.length());
    }

    @NotNull
    private String removeOptionalSuffix(@NotNull String specName) {
        if (specName.endsWith(OPTIONAL_TEST_SUFFIX)) {
            return removeSuffix(specName, OPTIONAL_TEST_SUFFIX);
        } else if (specName.endsWith(OPTIONAL_FIXTURE_SUFFIX)) {
            return removeSuffix(specName, OPTIONAL_FIXTURE_SUFFIX);
        } else {
            return specName;
        }
    }

    @NotNull
    private static String getIdentityKey(@NotNull PsiFile file) {
        return file.getVirtualFile().getPath();
    }
}
