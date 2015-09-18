package com.gman.idea.plugin.concordion.reference;

import com.gman.idea.plugin.concordion.lang.ConcordionLanguage;
import com.gman.idea.plugin.concordion.lang.psi.*;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ConcordionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        registrar.registerReferenceProvider(
                psiElement(ConcordionTypes.FIELD).withLanguage(ConcordionLanguage.INSTANCE),
                new MemberReferenceProvider()
        );

        registrar.registerReferenceProvider(
                psiElement(ConcordionTypes.METHOD).withLanguage(ConcordionLanguage.INSTANCE),
                new MemberReferenceProvider()
        );

        registrar.registerReferenceProvider(
                psiElement(ConcordionTypes.VARIABLE).withLanguage(ConcordionLanguage.INSTANCE),
                new VariableReferenceProvider()
        );
    }

    private static final class MemberReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            if (!(element instanceof ConcordionPsiElement)) {
                return PsiReference.EMPTY_ARRAY;
            }

            ConcordionPsiElement concordionPsiElement = (ConcordionPsiElement) element;
            PsiMember containingMember = concordionPsiElement.getContainingMember();
            if (containingMember == null) {
                return PsiReference.EMPTY_ARRAY;
            }

            return new PsiReference[] {
                    new ConcordionReference<>(concordionPsiElement, containingMember)
            };
        }
    }

    private static final class VariableReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            return PsiReference.EMPTY_ARRAY;
        }
    }
}
