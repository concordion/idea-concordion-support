package com.gman.idea.plugin.concordion;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;

import java.util.List;
import java.util.Map;

import static com.intellij.psi.search.ProjectScope.getProjectScope;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class Concordion {

    public static final String NAMESPACE = "http://www.concordion.org/2007/concordion";
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

    public static boolean isConcordionHtmlSpec(PsiFile psiFile) {
        return HtmlFileType.INSTANCE.equals(psiFile.getFileType())
                && psiFile.getText().contains(NAMESPACE);
    }

    public static String concordionSchemaPrefixOf(HtmlFileImpl psiFile) {
        XmlTag rootTag = psiFile.getRootTag();
        if (rootTag == null) {
            return null;
        }
        for (Map.Entry<String, String> declaration : rootTag.getLocalNamespaceDeclarations().entrySet()) {
            if (NAMESPACE.equalsIgnoreCase(declaration.getValue())) {
                return declaration.getKey();
            }
        }
        return null;
    }

    private static final String OPTIONAL_CONCORDION_SUFFIX_FOR_RUNNER_CLASS = "Test";

    public static PsiClass correspondingJavaClass(PsiFile htmlSpec) {
        Project project = htmlSpec.getProject();
        GlobalSearchScope scope = getProjectScope(project);

        String className = htmlSpec.getName().substring(0, htmlSpec.getName().indexOf('.'));
        String packageName = JavaDirectoryService.getInstance().getPackage(htmlSpec.getContainingDirectory()).getQualifiedName();
        String qualifiedName = packageName + '.' + className;

        PsiClass aClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName, scope);
        if (aClass == null) {
            qualifiedName = qualifiedName + OPTIONAL_CONCORDION_SUFFIX_FOR_RUNNER_CLASS;
            aClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName, scope);
        }

        return aClass;
    }

    private static final String JUNIT_RUN_WITH_ANNOTATION = "org.junit.runner.RunWith";
    private static final String BASIC_CONCORDION_RUNNER = "org.concordion.integration.junit4.ConcordionRunner";

    public static boolean classAnnotatedWithConcordionRunner(PsiClass runnerClass) {
        PsiAnnotation runner = runnerClass.getModifierList().findAnnotation(JUNIT_RUN_WITH_ANNOTATION);
        if (runner == null) {
            return false;
        }
        PsiJavaCodeReferenceElement runnerReference = PsiTreeUtil.findChildOfType(runner.getParameterList(), PsiJavaCodeReferenceElement.class);
        if (runnerReference == null) {
            return false;
        }

        Project project = runnerClass.getProject();
        GlobalSearchScope scope = getProjectScope(project);

        PsiClassType basicRunner = PsiType.getTypeByName(BASIC_CONCORDION_RUNNER, project, scope);
        PsiClassType usedRunner = PsiType.getTypeByName(runnerReference.getQualifiedName(), project, scope);

        return basicRunner.isConvertibleFrom(usedRunner);
    }

    private Concordion() {
    }
}
