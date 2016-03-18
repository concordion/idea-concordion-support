package org.concordion.plugin.idea.reference;

import org.concordion.plugin.idea.lang.psi.*;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class ConcordionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        registrar.registerReferenceProvider(
                concordionElement(ConcordionMember.class),
                new MemberReferenceProvider()
        );

        registrar.registerReferenceProvider(
                concordionElement(ConcordionVariable.class),
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
