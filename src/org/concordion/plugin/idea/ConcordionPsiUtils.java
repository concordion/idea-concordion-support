package org.concordion.plugin.idea;

import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionNext;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.concordion.plugin.idea.lang.psi.ConcordionPsiElement;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.psi.PsiModifier.*;
import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public final class ConcordionPsiUtils {

    private ConcordionPsiUtils() {
    }

    public static final PsiType DYNAMIC = new PsiPrimitiveType("?", new PsiAnnotation[0]);

    @NotNull
    public static List<PsiType> typeOfExpressions(@NotNull List<ConcordionOgnlExpressionStart> starts) {
        return starts.stream().map(ConcordionPsiUtils::typeOfExpression).collect(toList());
    }

    @Nullable
    public static PsiType typeOfExpression(@NotNull ConcordionOgnlExpressionStart start) {
        if (start.getOgnlExpressionNext() != null) {
            return typeOfChain(start.getOgnlExpressionNext());
        } else {
            ConcordionPsiElement typedElement = firstNotNullIfPresent(start.getMethod(), start.getField(), start.getVariable());
            if (typedElement != null) {
                return typedElement.getType();
            }
            if (start.getLiteral() != null) {
                //PsiType.NULL means resolved, but can be dynamically typed to Integer/Double/String
                return ConcordionPsiUtils.DYNAMIC;
            }
            return null;
        }
    }

    @Nullable
    private static PsiType typeOfChain(@NotNull ConcordionOgnlExpressionNext next) {
        Iterator<ConcordionOgnlExpressionNext> following = next.getOgnlExpressionNextList().iterator();
        if (following.hasNext()) {
            return typeOfChain(following.next());
        } else {
            ConcordionPsiElement typedElement = firstNotNullIfPresent(next.getMethod(), next.getField());
            if (typedElement != null) {
                return typedElement.getType();
            }
            return null;
        }
    }

    public static int arrayDimensionsUsed(@NotNull ConcordionPsiElement concordionPsiElement) {
        ConcordionOgnlExpressionNext next = getParentOfType(concordionPsiElement, ConcordionOgnlExpressionNext.class);
        if (next != null) {
            return next.getIndexList().size();
        }
        ConcordionOgnlExpressionStart start = getParentOfType(concordionPsiElement, ConcordionOgnlExpressionStart.class);
        if (start != null) {
            return start.getIndexList().size();
        }
        return 0;
    }

    @Nullable
    public static PsiMethod findMethodInClass(@NotNull PsiClass containingClass, @Nullable String name, @NotNull List<PsiType> arguments) {
        return stream(containingClass.getAllMethods())
                .filter(m -> m.getName().equals(name))
                .filter(m -> argumentTypesMatchParameterTypes(m.getParameterList().getParameters(), arguments))
                .filter(ConcordionPsiUtils::concordionVisibleMethod)
                .findFirst().orElse(null);
    }

    private static boolean argumentTypesMatchParameterTypes(@NotNull PsiParameter[] parameters, @NotNull List<PsiType> arguments) {
        if (parameters.length == 0) {
            return arguments.isEmpty();
        }

        int lastParameterIndex = parameters.length - 1;
        for (int p = 0; p < lastParameterIndex; p++) {
            if (!validArgumentType(arguments.get(p), parameters[p].getType())) {
                return false;
            }
        }
        if (!parameters[lastParameterIndex].isVarArgs()) {
            return parameters.length == arguments.size()
                    && validArgumentType(arguments.get(lastParameterIndex), parameters[lastParameterIndex].getType());
        } else {
            //array is passed into vararg
            if (parameters.length == arguments.size() && validArgumentType(arguments.get(lastParameterIndex), parameters[lastParameterIndex].getType())) {
                return true;
            }
            PsiType last = parameters[lastParameterIndex].getType().getDeepComponentType();
            for (int a = lastParameterIndex; a < arguments.size(); a++) {
                if (!validArgumentType(arguments.get(a), last)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean validArgumentType(PsiType argument, PsiType parameter) {
        return argument == null
                || argument == DYNAMIC
                || parameter.isAssignableFrom(argument);
    }

    @Nullable
    public static PsiField findFieldInClass(@NotNull PsiClass containingClass, @Nullable String name) {
        return stream(containingClass.getAllFields())
                .filter(f -> f.getName().equals(name))
                .filter(ConcordionPsiUtils::concordionVisibleField)
                .findFirst().orElse(null);
    }

    @Nullable
    public static PsiAnnotation findAnnotationInClassHierarchy(@NotNull PsiClass psiClass, @NotNull String qualifiedName) {
        for (PsiClass current = psiClass; current != null ; current = current.getSuperClass()) {
            PsiModifierList modifiers = current.getModifierList();
            if (modifiers == null) {
                continue;
            }
            PsiAnnotation annotation = modifiers.findAnnotation(qualifiedName);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    @Nullable
    public static String runnerQualifiedNameFromRunWithAnnotation(@NotNull PsiAnnotation runWith) {
        PsiJavaCodeReferenceElement runner = findChildOfType(runWith.getParameterList(), PsiJavaCodeReferenceElement.class);
        if (runner == null) {
            return null;
        }
        return runner.getQualifiedName();
    }

    public static final String CONCORDION_FULL_OGNL = "org.concordion.api.FullOGNL";
    public static final String JUNIT_RUN_WITH_ANNOTATION = "org.junit.runner.RunWith";
    public static final String CONCORDION_RUNNER = "org.concordion.integration.junit4.ConcordionRunner";


    public static boolean isConcordionFixture(@NotNull PsiClass psiClass) {
        PsiAnnotation runWithAnnotation = ConcordionPsiUtils.findAnnotationInClassHierarchy(psiClass, JUNIT_RUN_WITH_ANNOTATION);
        if (runWithAnnotation == null) {
            return false;
        }
        String runnerQualifiedName = runnerQualifiedNameFromRunWithAnnotation(runWithAnnotation);
        return CONCORDION_RUNNER.equals(runnerQualifiedName);
    }

    public static boolean concordionVisibleField(@NotNull PsiField psiField) {
        PsiModifierList modifiers = psiField.getModifierList();
        return modifiers != null
                && modifiers.hasModifierProperty(PUBLIC)
                && !modifiers.hasModifierProperty(STATIC);
    }

    public static boolean concordionVisibleMethod(@NotNull PsiMethod psiMethod) {
        return psiMethod.getModifierList().hasModifierProperty(PUBLIC)
                && !psiMethod.isConstructor();//Yes, static methods are accepted, static fields are not
    }

    @Nullable
    public static String memberIdentity(@NotNull PsiMember member) {
        return member.getContainingClass() != null
                ? member.getContainingClass().getQualifiedName() + ':' + member.getName()
                : member.getName();
    }

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

    @NotNull
    public static String nullToEmpty(@Nullable String str) {
        return str != null ? str : "";
    }

    @Nullable
    public static <T> T firstNotNullIfPresent(@NotNull T... elements) {
        return stream(elements).filter(Objects::nonNull).findFirst().orElse(null);
    }

    @NotNull
    public static <T> Set<T> setOf(@NotNull T... elements) {
        //should I use google guava just for this?
        if (elements.length == 1) {
            return singleton(elements[0]);
        }
        return new HashSet<>(asList(elements));
    }
}
