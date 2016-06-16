package org.concordion.plugin.idea;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.classIn;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.*;

public class ConcordionNavigationService {

    public static ConcordionNavigationService getInstance(Project project) {
        return ServiceManager.getService(project, ConcordionNavigationService.class);
    }

    private static final String OPTIONAL_TEST_SUFFIX = "Test";
    private static final String OPTIONAL_FIXTURE_SUFFIX = "Fixture";

    private final Set<String> specExtensions = allRegisteredExtensions(specifications());
    private final Set<String> testFixtureExtensions = allRegisteredExtensions(fixtures());

    private final PsiElementCache<PsiFile> cache = new PsiElementCache<>(ConcordionNavigationService::getIdentityKey);

    @Nullable
    public PsiClass correspondingTestFixture(@Nullable PsiFile spec) {
        if (!canBeNavigated(spec)) {
            return null;
        }

        String specName = removeExtension(spec.getName());

        if (!specName.equals(removeOptionalSuffix(specName))) {
            return null;
        }

        PsiClass testFixture = classIn(
                cache.getOrCompute(getIdentityKey(spec), () -> findCorrespondingSpecFile(
                        spec.getContainingDirectory(),
                        possibleFixtures(specName)
                ))
        );

        return isConcordionSpecAndFixture(spec, testFixture) ? testFixture : null;
    }

    @Nullable
    public PsiFile correspondingSpec(@Nullable PsiClass testFixture) {
        if (testFixture == null) {
            return null;
        }

        PsiFile javaFile = testFixture.getContainingFile();
        if (!canBeNavigated(javaFile)) {
            return null;
        }

        String specName = removeOptionalSuffix(removeExtension(javaFile.getName()));

        PsiFile spec = cache.getOrCompute(getIdentityKey(javaFile), () -> findCorrespondingSpecFile(
                javaFile.getContainingDirectory(),
                possibleSpecs(specName)
        ));

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
        return testFixtureExtensions.contains(file.getFileType().getDefaultExtension())
                ? correspondingSpec(classIn(file))
                : getParentOfType(correspondingTestFixture(file), PsiFile.class);
    }

    @NotNull
    private Collection<String> possibleFixtures(@NotNull String specName) {
        return testFixtureExtensions.stream()
                .flatMap(ext -> Stream.of(
                        specName + '.' + ext,
                        specName + OPTIONAL_TEST_SUFFIX + '.' + ext,
                        specName + OPTIONAL_FIXTURE_SUFFIX + '.' + ext
                ))
                .collect(toList());
    }

    @NotNull
    private Collection<String> possibleSpecs(@NotNull String specName) {
        return specExtensions.stream()
                .map(ext -> specName + '.' + ext)
                .collect(toList());
    }


    @Nullable
    private PsiFile findCorrespondingSpecFile(@NotNull PsiDirectory oneOfPackageDirs, @NotNull Collection<String> names) {
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
    private PsiFile findFirstFile(@NotNull PsiDirectory directory, @NotNull Collection<String> names) {
        return names.stream()
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

    @NotNull
    private static Set<String> allRegisteredExtensions(@NotNull Stream<? extends ConcordionExtension> extensions) {
        return extensions
                .flatMap(ConcordionExtension::fileExtensionsAsStream)
                .collect(toSet());
    }
}
