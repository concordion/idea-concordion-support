package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.ConcordionFileType;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlTag;
import com.intellij.testFramework.LightVirtualFileBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.intellij.psi.search.ProjectScope.getProjectScope;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class Concordion {

    public static final List<String> COMMANDS = unmodifiableList(asList(
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "echo",
            "execute",
            "run",
            "set",
            "verifyRows", "verify-rows",
            "example"
    ));

    private static final String CONCORDION_NAMESPACE = "http://www.concordion.org/2007/concordion";

    public static boolean isConcordionHtmlSpec(@NotNull PsiFile psiFile) {
        return HtmlFileType.INSTANCE.equals(psiFile.getFileType())
                && psiFile.getText().contains(CONCORDION_NAMESPACE);//TODO better check
    }

    public static boolean isConcordionNamespace(String namespace) {
        return CONCORDION_NAMESPACE.equalsIgnoreCase(namespace);
    }

    @Nullable
    public static String concordionSchemaPrefixOf(@NotNull PsiFile psiFile) {
        if (!(psiFile instanceof HtmlFileImpl)) {
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

    private static final String OPTIONAL_CONCORDION_SUFFIX_FOR_RUNNER_CLASS = "Test";

    @Nullable
    public static PsiClass correspondingJavaRunner(@Nullable PsiFile htmlSpec) {
        //TODO seems to be used a lot. Cache?
        if (htmlSpec == null || htmlSpec.getContainingDirectory() == null) {
            return null;
        }

        Project project = htmlSpec.getProject();
        GlobalSearchScope scope = getProjectScope(project);

        String className = htmlSpec.getName().substring(0, htmlSpec.getName().indexOf('.'));
        PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(htmlSpec.getContainingDirectory());
        if (aPackage == null) {
            return null;
        }
        String qualifiedName = aPackage.getQualifiedName() + '.' + className;

        PsiClass runnerClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName, scope);
        if (runnerClass == null) {
            qualifiedName = qualifiedName + OPTIONAL_CONCORDION_SUFFIX_FOR_RUNNER_CLASS;
            runnerClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName, scope);
        }

        return runnerClass;
    }

    @Nullable
    public static PsiFile correspondingHtmlSpec(@Nullable PsiClass runnerClass) {
        if (runnerClass == null) {
            return null;
        }

        String specName = runnerClass.getName();
        String noTestSpecName = specName.endsWith(OPTIONAL_CONCORDION_SUFFIX_FOR_RUNNER_CLASS) ?
                specName.substring(0, specName.length() - OPTIONAL_CONCORDION_SUFFIX_FOR_RUNNER_CLASS.length()) + '.' + HtmlFileType.INSTANCE.getDefaultExtension() : null;

        specName +='.' + HtmlFileType.INSTANCE.getDefaultExtension();

        PsiDirectory classDirectory = runnerClass.getContainingFile().getContainingDirectory();
        PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(classDirectory);
        if (aPackage == null) {
            return null;
        }
        for (PsiDirectory packageDirectory : aPackage.getDirectories()) {
            PsiFile htmlSpec = packageDirectory.findFile(specName);
            if (htmlSpec != null) {
                return htmlSpec;
            }
            if (noTestSpecName == null) {
                continue;
            }
            htmlSpec = packageDirectory.findFile(noTestSpecName);
            if (htmlSpec != null) {
                return htmlSpec;
            }
        }

        return null;
    }

    @Nullable
    public static PsiFile unpackSpecFromLanguageInjection(@NotNull PsiFile originalFile) {
        if (!ConcordionFileType.INSTANCE.equals(originalFile.getFileType())) {
            return originalFile;
        }

        VirtualFile file = originalFile.getVirtualFile();

        while (file != null && file.getParent() == null) {
            file = getDelegate(file);
        }

        if (file != null) {
            return PsiManager.getInstance(originalFile.getProject()).findFile(file);
        }
        return null;
    }

    @Nullable
    private static VirtualFile getDelegate(@Nullable VirtualFile virtualFile) {
        if (virtualFile instanceof VirtualFileWindow) {
            //Language injection in xml
            return ((VirtualFileWindow) virtualFile).getDelegate();
        } else if (virtualFile instanceof LightVirtualFileBase) {
            //In fragment editor window
            return ((LightVirtualFileBase) virtualFile).getOriginalFile();
        } else {
            return null;
        }
    }

    private static final String JUNIT_RUN_WITH_ANNOTATION = "org.junit.runner.RunWith";
    private static final String BASIC_CONCORDION_RUNNER = "org.concordion.integration.junit4.ConcordionRunner";

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
        if (runnerReference == null) {
            return false;
        }

        Project project = runWithAnnotation.getProject();
        GlobalSearchScope scope = getProjectScope(project);

        PsiClassType basicRunner = PsiType.getTypeByName(BASIC_CONCORDION_RUNNER, project, scope);
        PsiClassType usedRunner = PsiType.getTypeByName(runnerReference.getQualifiedName(), project, scope);

        return basicRunner.isConvertibleFrom(usedRunner);
    }


    private Concordion() {
    }
}
