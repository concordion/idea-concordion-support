package com.gman.idea.plugin.concordion;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcordionNavigationService {

    public static ConcordionNavigationService getInstance(Project project) {
        return ServiceManager.getService(project, ConcordionNavigationService.class);
    }

    private final PsiElementCache<PsiFile> cache = new PsiElementCache<>();

    @Nullable
    public  PsiClass correspondingJavaRunner(@Nullable PsiFile htmlSpec) {
        return PsiTreeUtil.getChildOfType(correspondingSpecFile(htmlSpec), PsiClass.class);
    }

    @Nullable
    public  PsiFile correspondingHtmlSpec(@Nullable PsiClass runnerClass) {
        return runnerClass != null ? correspondingSpecFile(runnerClass.getContainingFile()) : null;
    }

    @Nullable
    public  PsiFile correspondingSpecFile(@Nullable PsiFile file) {
        if (file == null) {
            return null;
        }

        String path = file.getVirtualFile().getPath();
        PsiFile cachedFile = cache.get(path);
        if (cachedFile != null) {
            return cachedFile;
        }

        PsiFile correspondingSpecFile = findCorrespondingSpecFile(file);
        if (correspondingSpecFile == null) {
            return null;
        }

        cache.put(path, correspondingSpecFile);
        return correspondingSpecFile;
    }

    private  final String OPTIONAL_TEST_SUFFIX = "Test";

    @Nullable
    private PsiFile findCorrespondingSpecFile(@NotNull PsiFile file) {
        if (file.getContainingDirectory() == null
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

    private  boolean isConcordionType(@NotNull FileType type) {
        return JavaFileType.INSTANCE.equals(type) || HtmlFileType.INSTANCE.equals(type);
    }

    @NotNull
    private  String extractSpecNameNoTest(@NotNull String fileName) {
        String specName = fileName.substring(0, fileName.indexOf('.'));
        if (specName.endsWith(OPTIONAL_TEST_SUFFIX)) {
            specName = specName.substring(0, specName.length() - OPTIONAL_TEST_SUFFIX.length());
        }
        return specName;
    }

    @NotNull
    private  FileType pairedType(@NotNull FileType type) {
        if (JavaFileType.INSTANCE.equals(type)) {
            return HtmlFileType.INSTANCE;
        } else if (HtmlFileType.INSTANCE.equals(type)) {
            return JavaFileType.INSTANCE;
        } else {
            throw new IllegalArgumentException(type.getDefaultExtension() + " is not allowed here!");
        }
    }

    @Nullable
    private  PsiFile findFirstFileInPackage(@NotNull PsiPackage aPackage, @NotNull String... names) {
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
}
