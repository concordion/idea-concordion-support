package com.gman.idea.plugin.concordion.reference;

import com.gman.idea.plugin.concordion.lang.ConcordionLanguage;
import com.gman.idea.plugin.concordion.lang.psi.*;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.gman.idea.plugin.concordion.Concordion.correspondingJavaRunner;
import static com.gman.idea.plugin.concordion.Concordion.unpackSpecFromLanguageInjection;
import static java.util.Arrays.stream;

public class ConcordionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(ConcordionTypes.FIELD).withLanguage(ConcordionLanguage.INSTANCE),
                new FieldReferenceProvider()
        );

        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(ConcordionTypes.METHOD).withLanguage(ConcordionLanguage.INSTANCE),
                new MethodReferenceProvider()
        );

        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(ConcordionTypes.VARIABLE).withLanguage(ConcordionLanguage.INSTANCE),
                new VariableReferenceProvider()
        );
    }

    private static final class VariableReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            return PsiReference.EMPTY_ARRAY;
        }
    }

    private static final class MethodReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            PsiFile containingFile = unpackSpecFromLanguageInjection(element.getContainingFile());
            PsiClass psiClass = correspondingJavaRunner(containingFile);

            if (containingFile == null || psiClass == null) {
                return PsiReference.EMPTY_ARRAY;
            }

            Optional<PsiMethod> method = findMethod(psiClass, (ConcordionMethod) element);

            if (!method.isPresent()) {
                return PsiReference.EMPTY_ARRAY;
            }

            return new PsiReference[] {
                    new ConcordionReference<>(element, method.get())
            };
        }

        private Optional<PsiMethod> findMethod(PsiClass clazz, ConcordionMethod method) {
            String name = method.getMethodName();
            int paramsCount = method.getMethodParametersCount();
            return stream(clazz.getAllMethods()).filter(m -> m.getName().equals(name) && m.getParameterList().getParametersCount() == paramsCount).findFirst();
        }
    }

    private static final class FieldReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            PsiFile containingFile = unpackSpecFromLanguageInjection(element.getContainingFile());
            PsiClass psiClass = correspondingJavaRunner(containingFile);

            if (containingFile == null || psiClass == null) {
                return PsiReference.EMPTY_ARRAY;
            }

            Optional<PsiField> field = findField(psiClass, (ConcordionField) element);

            if (!field.isPresent()) {
                return PsiReference.EMPTY_ARRAY;
            }

            return new PsiReference[] {
                    new ConcordionReference<>(element, field.get())
            };
        }

        private Optional<PsiField> findField(PsiClass clazz, ConcordionField field) {
            String name = field.getFieldName();
            return stream(clazz.getAllFields()).filter(f -> f.getName().equals(name)).findFirst();
        }
    }
}
