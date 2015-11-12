package org.concordion.plugin.idea.reference;

import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.lang.psi.*;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ConcordionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        registrar.registerReferenceProvider(
                psiElement(ConcordionMember.class).withLanguage(ConcordionLanguage.INSTANCE),
                new MemberReferenceProvider()
        );

        registrar.registerReferenceProvider(
                psiElement(ConcordionVariable.class).withLanguage(ConcordionLanguage.INSTANCE),
                new VariableReferenceProvider()
        );
    }

    private static final class MemberReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

            return new PsiReference[] {
                    new ConcordionMemberReference((ConcordionMember) element)
            };
        }
    }

    private static final class VariableReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

            return new PsiReference[] {
                    new ConcordionVariableReference((ConcordionVariable) element)
            };
        }
    }
}
