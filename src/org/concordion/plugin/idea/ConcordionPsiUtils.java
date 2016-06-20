package org.concordion.plugin.idea;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.ide.highlighter.XHtmlFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import org.concordion.plugin.idea.lang.ConcordionFileType;
import org.concordion.plugin.idea.lang.psi.*;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static com.intellij.psi.PsiModifier.*;
import static com.intellij.psi.util.PsiTreeUtil.*;
import static java.lang.Character.*;
import static java.util.Arrays.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import static org.concordion.plugin.idea.ConcordionCommand.*;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;

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
            ConcordionPsiElement typedElement = firstNotNull(start::getMethod, start::getField, start::getVariable);
            if (typedElement != null) {
                return typedElement.getType();
            }
            if (start.getLiteral() != null) {
                //TODO type of literal: start.getLiteral().getNode().getChildren(null)[0].getElementType()
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

    @Nullable
    private static PsiType typeOfChain(@NotNull ConcordionOgnlExpressionNext next) {
        Iterator<ConcordionOgnlExpressionNext> following = next.getOgnlExpressionNextList().iterator();
        if (following.hasNext()) {
            return typeOfChain(following.next());
        } else {
            ConcordionPsiElement typedElement = firstNotNull(next::getMethod, next::getField);
            if (typedElement != null) {
                return typedElement.getType();
            }
            return null;
        }
    }

    @Nullable
    public static String nameInExpression(@NotNull ConcordionOgnlExpressionStart start) {
        ConcordionPsiElement namedElement = firstNotNull(start::getMethod, start::getField, start::getVariable);
        return namedElement != null ? namedElement.getName() : null;
    }

    @Nullable
    public static String commandText(@Nullable ConcordionEmbeddedCommand command) {
        if (command == null) {
            return null;
        }
        String text = command.getText();
        if ("?=".equals(text)) {
            return ASSERT_EQUALS_SPINAL.text();
        }
        int prefix = text.indexOf(':');
        int assignment = text.indexOf('=');
        return text.substring(prefix + 1, assignment);
    }

    @Nullable
    public static String embeddedCommandTextOf(@NotNull PsiElement injected) {
        return commandText(
                findChildOfType(
                        getParentOfType(injected, ConcordionStatement.class),
                        ConcordionEmbeddedCommand.class
                )
        );
    }

    @Nullable
    public static String attributeCommandTextOf(@Nullable PsiElement concordionXmlFragment) {
        XmlAttribute attribute = PsiTreeUtil.getParentOfType(concordionXmlFragment, XmlAttribute.class, false);
        if (attribute == null) {
            return null;
        }
        return attribute.getLocalName();
    }

    @Nullable
    public static String commandTextOf(@NotNull PsiElement element) {
        FileType fileType = element.getContainingFile().getFileType();
        if (ConcordionFileType.INSTANCE.equals(fileType)) {
            return firstNotNull(
                    () -> embeddedCommandTextOf(element),
                    () -> attributeCommandTextOf(InjectedLanguageUtil.findInjectionHost(element))
            );
        } else if (HtmlFileType.INSTANCE.equals(fileType) || XHtmlFileType.INSTANCE.equals(fileType)) {
            return attributeCommandTextOf(element);
        } else {
            return null;
        }
    }

    @NotNull
    public static ConcordionCommand commandOf(@NotNull PsiElement element) {
        return ofNullable(commandTextOf(element)).map(ConcordionCommand::findCommandByText).orElse(EXECUTE);
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
    public static String getterFor(@NotNull String name) {
        return "get" + (name.length() >= 2 && isUpperCase(name.charAt(1)) ? name : toUpperCase(name.charAt(0)) + name.substring(1));
    }

    @Nullable
    public static PsiClass classIn(@Nullable PsiFile file) {
        return findChildOfType(file, PsiClass.class);
    }

    @NotNull
    public static String removeExtension(@NotNull String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    @NotNull
    public static String nullToEmpty(@Nullable String str) {
        return str != null ? str : "";
    }

    @Nullable
    public static <T> T firstNotNull(@NotNull Supplier<T>... suppliers) {
        return stream(suppliers)
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
