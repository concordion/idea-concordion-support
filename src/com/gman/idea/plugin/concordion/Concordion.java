package com.gman.idea.plugin.concordion;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class Concordion {

    public static final List<String> COMMANDS = unmodifiableList(asList(
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "execute",
            "set",
            "echo",
            "verifyRows", "verify-rows",
            "matchStrategy", "match-strategy",
            "matchingRole", "matching-role",
            "run",
            "example"
    ));

    private static final String CONCORDION_NAMESPACE = "http://www.concordion.org/2007/concordion";

    public static boolean isConcordionHtmlSpec(@NotNull PsiFile psiFile) {
        return concordionSchemaPrefixOf(psiFile) != null;
    }

    public static boolean isConcordionNamespace(@Nullable String namespace) {
        return CONCORDION_NAMESPACE.equalsIgnoreCase(namespace);
    }

    @Nullable
    public static String concordionSchemaPrefixOf(@NotNull PsiFile psiFile) {
        if (!HtmlFileType.INSTANCE.equals(psiFile.getFileType())) {
            return null;
        }
        XmlTag rootTag = ((HtmlFileImpl) psiFile).getRootTag();
        if (rootTag == null) {
            return null;
        }
        for (Map.Entry<String, String> declaration : rootTag.getLocalNamespaceDeclarations().entrySet()) {
            if (CONCORDION_NAMESPACE.equalsIgnoreCase(declaration.getValue())) {
                return declaration.getKey();
            }
        }
        return null;
    }

    private static final String OPTIONAL_TEST_SUFFIX = "Test";

    @Nullable
    public static PsiFile correspondingSpecFile(@Nullable PsiFile file) {
        //TODO seems to be used a lot. Cache?
        if (file == null
                || file.getContainingDirectory() == null
                || !isConcordionType(file.getFileType())) {
            return null;
        }

        PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(file.getContainingDirectory());
        if (aPackage == null) {
            return null;
        }

        String specName = extractSpecNameNoTest(file.getName());
        String extension = pairedType(file.getFileType()).getDefaultExtension();

        return findFirstFileInPackage(aPackage, specName + '.' + extension, specName + OPTIONAL_TEST_SUFFIX + '.' + extension);
    }

    private static boolean isConcordionType(@NotNull FileType type) {
        return JavaFileType.INSTANCE.equals(type) || HtmlFileType.INSTANCE.equals(type);
    }

    @NotNull
    private static String extractSpecNameNoTest(@NotNull String fileName) {
        String specName = fileName.substring(0, fileName.indexOf('.'));
        if (specName.endsWith(OPTIONAL_TEST_SUFFIX)) {
            specName = specName.substring(0, specName.length() - OPTIONAL_TEST_SUFFIX.length());
        }
        return specName;
    }

    @NotNull
    private static FileType pairedType(@NotNull FileType type) {
        if (JavaFileType.INSTANCE.equals(type)) {
            return HtmlFileType.INSTANCE;
        } else if (HtmlFileType.INSTANCE.equals(type)) {
            return JavaFileType.INSTANCE;
        } else {
            throw new IllegalArgumentException(type.getDefaultExtension() + " is not allowed here!");
        }
    }

    @Nullable
    private static PsiFile findFirstFileInPackage(@NotNull PsiPackage aPackage, @NotNull String... names) {
        for (PsiDirectory directory : aPackage.getDirectories()) {
            for (String name : names) {
                PsiFile file = directory.findFile(name);
                if (file != null) {
                    return file;
                }
            }
        }
        return null;
    }

    @Nullable
    public static PsiClass correspondingJavaRunner(@Nullable PsiFile htmlSpec) {
        return PsiTreeUtil.getChildOfType(correspondingSpecFile(htmlSpec), PsiClass.class);
    }

    @Nullable
    public static PsiFile correspondingHtmlSpec(@Nullable PsiClass runnerClass) {
        return runnerClass != null ? correspondingSpecFile(runnerClass.getContainingFile()) : null;
    }

    private static final String JUNIT_RUN_WITH_ANNOTATION = RunWith.class.getName();
    private static final String BASIC_CONCORDION_RUNNER = ConcordionRunner.class.getName();
    private static final String USING_FULL_OGNL = FullOGNL.class.getName();

    public static boolean isUsingFullOgnl(@NotNull PsiClass runnerClass) {
        PsiModifierList modifierList = runnerClass.getModifierList();
        return modifierList != null && modifierList.findAnnotation(USING_FULL_OGNL) != null;
    }

    public static boolean isClassAnnotatedWithConcordionRunner(@NotNull PsiClass runnerClass) {
        PsiAnnotation runner = findJunitRunWithAnnotation(runnerClass);
        return runner != null && isRunWithAnnotationUsesConcordionRunner(runner);
    }

    public static PsiAnnotation findJunitRunWithAnnotation(@NotNull PsiClass runnerClass) {
        PsiModifierList modifierList = runnerClass.getModifierList();
        return modifierList != null ? modifierList.findAnnotation(JUNIT_RUN_WITH_ANNOTATION) : null;
    }

    public static boolean isJunitRunWithAnnotation(@NotNull PsiAnnotation runWithAnnotation) {
        return JUNIT_RUN_WITH_ANNOTATION.equals(runWithAnnotation.getQualifiedName());
    }

    public static boolean isRunWithAnnotationUsesConcordionRunner(@NotNull PsiAnnotation runWithAnnotation) {
        PsiJavaCodeReferenceElement runnerReference = findChildOfType(runWithAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class);
        return runnerReference != null && BASIC_CONCORDION_RUNNER.equals(runnerReference.getQualifiedName());
    }

    private Concordion() {
    }
}
