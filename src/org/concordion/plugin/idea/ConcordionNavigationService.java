package org.concordion.plugin.idea;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

import static java.util.Arrays.stream;
import static org.concordion.plugin.idea.ConcordionTestFixtureUtil.*;
import static org.concordion.plugin.idea.ConcordionSpecType.*;

public class ConcordionNavigationService {

    public static ConcordionNavigationService getInstance(Project project) {
        return ServiceManager.getService(project, ConcordionNavigationService.class);
    }

    private static final String OPTIONAL_TEST_SUFFIX = "Test";
    private static final String OPTIONAL_FIXTURE_SUFFIX = "Fixture";

    private static final String JAVA_EXTENSION = JavaFileType.DOT_DEFAULT_EXTENSION;
    private static final Set<String> POSSIBLE_SPEC_EXTENSIONS = allPossibleSpecExtensions();

    private final PsiElementCache<PsiFile> cache = new PsiElementCache<>(ConcordionNavigationService::getIdentityKey);

    @Nullable
    public  PsiClass correspondingTestFixture(@Nullable PsiFile spec) {
        PsiClass testFixture = PsiTreeUtil.getChildOfType(correspondingJavaFile(spec), PsiClass.class);
        return isConcordionSpecAndFixture(spec, testFixture) ? testFixture : null;
    }

    @Nullable
    public  PsiFile correspondingSpec(@Nullable PsiClass testFixture) {
        if (testFixture == null) {
            return null;
        }
        PsiFile spec = correspondingSpecFile(testFixture.getContainingFile());
        return isConcordionSpecAndFixture(spec, testFixture) ? spec : null;
    }

    public void navigateToPairedFile(@Nullable PsiFile file) {
        if (file == null) {
            return;
        }

        PsiFile paired = pairedFile(file);
        if (paired == null || !paired.canNavigate()) {
            return;
        }

        paired.navigate(true);
    }

    @Nullable
    public PsiFile pairedFile(@NotNull PsiFile file) {
        return JavaFileType.INSTANCE.equals(file.getFileType())
                ? correspondingSpecFile(file)
                : correspondingJavaFile(file);
    }

    @Nullable
    private PsiFile correspondingJavaFile(@Nullable PsiFile specFile) {
        if (!canBeNavigated(specFile)) {
            return null;
        }

        String specName = removeExtension(specFile.getName());

        if (!specName.equals(removeOptionalSuffix(specName))) {
            return null;
        }

        return cache.getOrCompute(getIdentityKey(specFile), () -> findCorrespondingSpecFile(
                specFile.getContainingDirectory(),
                specName + JAVA_EXTENSION,
                specName + OPTIONAL_TEST_SUFFIX + JAVA_EXTENSION,
                specName + OPTIONAL_FIXTURE_SUFFIX + JAVA_EXTENSION
        ));
    }

    @Nullable
    private PsiFile correspondingSpecFile(@Nullable PsiFile javaFile) {
        if (!canBeNavigated(javaFile)) {
            return null;
        }

        String specName = removeOptionalSuffix(removeExtension(javaFile.getName()));

        return cache.getOrCompute(getIdentityKey(javaFile), () -> findCorrespondingSpecFile(
                javaFile.getContainingDirectory(),
                possibleSpecs(specName)
        ));
    }

    @NotNull
    private String[] possibleSpecs(String specName) {
        return POSSIBLE_SPEC_EXTENSIONS.stream()
                .map(ext -> specName + '.' + ext)
                .toArray(String[]::new);
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
        return file != null
                && file.getContainingDirectory() != null
                && file.getName().lastIndexOf('.') != -1;
    }

    private boolean isConcordionSpecAndFixture(@Nullable PsiFile spec, @Nullable PsiClass fixture) {
        return spec != null
                && fixture != null
                && (specConfiguredInFile(spec) || isConcordionFixture(fixture));
    }

    @NotNull
    private String removeExtension(@NotNull String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
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
