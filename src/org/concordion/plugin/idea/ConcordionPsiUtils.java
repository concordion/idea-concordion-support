package org.concordion.plugin.idea;

import org.concordion.plugin.idea.lang.psi.ConcordionEmbeddedCommand;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionNext;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.concordion.plugin.idea.lang.psi.ConcordionPsiElement;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.psi.PsiModifier.*;
import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.util.Arrays.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.findList;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.findMap;

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
                return DYNAMIC;
            }
            if (start.getList() != null) {
                return findList(start.getProject());
            }
            if (start.getMap() != null) {
                return findMap(start.getProject());
            }
            return null;
        }
    }

    @NotNull
    public static String commandText(@Nullable ConcordionEmbeddedCommand command) {
        if (command == null) {
            return "set";
        }
        String text = command.getText();
        if ("?=".equals(text)) {
            return "assertEquals";
        }
        int prefix = text.indexOf(':');
        int assignment = text.indexOf('=');
        return text.substring(prefix+1, assignment);
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
        if (!isVarArg(parameters)) {
            if (parameters.length != arguments.size()) {
                return false;
            }
            for (int i = 0; i < parameters.length; i++) {
                if (!validArgumentType(arguments.get(i), parameters[i].getType())) {
                    return false;
                }
            }
            return true;
        } else {
            int varArgIndex = parameters.length - 1;
            for (int i = 0; i < varArgIndex; i++) {
                if (!validArgumentType(arguments.get(i), parameters[i].getType())) {
                    return false;
                }
            }
            //array passed into vararg
            if (parameters.length == arguments.size() && validArgumentType(arguments.get(varArgIndex), parameters[varArgIndex].getType())) {
                return true;
            }
            PsiType last = parameters[varArgIndex].getType().getDeepComponentType();
            for (int i = varArgIndex; i < arguments.size(); i++) {
                if (!validArgumentType(arguments.get(i), last)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean isVarArg(@NotNull PsiParameter[] parameters) {
        int lastOne = parameters.length - 1;
        return lastOne >= 0 && parameters[lastOne].isVarArgs();
    }

    private static boolean validArgumentType(@Nullable PsiType argument, @NotNull PsiType parameter) {
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

    public static final String CONCORDION_FULL_OGNL = "org.concordion.api.FullOGNL";
    public static final String JUNIT_RUN_WITH_ANNOTATION = "org.junit.runner.RunWith";
    public static final String CONCORDION_RUNNER = "org.concordion.integration.junit4.ConcordionRunner";
    public static final String CONCORDION_EXTENSIONS_ANNOTATION = "org.concordion.api.extension.Extensions";

    public static boolean isConcordionFixture(@NotNull PsiClass testFixture) {
        PsiAnnotation runWithAnnotation = ConcordionPsiUtils.findAnnotationInClassHierarchy(testFixture, JUNIT_RUN_WITH_ANNOTATION);
        if (runWithAnnotation == null) {
            return false;
        }
        PsiJavaCodeReferenceElement runner = findChildOfType(runWithAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class);
        return runner != null && CONCORDION_RUNNER.equals(runner.getQualifiedName());
    }

    public static Collection<String> configuredExtensions(@NotNull PsiClass testFixture) {
        PsiAnnotation extensionsAnnotation = ConcordionPsiUtils.findAnnotationInClassHierarchy(testFixture, CONCORDION_EXTENSIONS_ANNOTATION);
        if (extensionsAnnotation == null) {
            return emptyList();
        }
        return findChildrenOfType(extensionsAnnotation.getParameterList(), PsiJavaCodeReferenceElement.class).stream()
                .map(PsiJavaCodeReferenceElement::getQualifiedName)
                .collect(toList());
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

    @NotNull
    public static String nullToEmpty(@Nullable String str) {
        return str != null ? str : "";
    }

    @Nullable
    public static <T> T firstNotNullIfPresent(@NotNull T... elements) {
        return stream(elements).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
