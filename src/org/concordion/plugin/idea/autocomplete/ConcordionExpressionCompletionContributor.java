package org.concordion.plugin.idea.autocomplete;

import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.lang.psi.ConcordionMember;
import org.concordion.plugin.idea.lang.psi.ConcordionOgnlExpressionStart;
import org.concordion.plugin.idea.lang.psi.ConcordionPsiElement;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import com.intellij.codeInsight.completion.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static org.concordion.plugin.idea.CompleteInformation.*;
import static com.intellij.patterns.PlatformPatterns.*;
import static org.concordion.plugin.idea.ConcordionElementPattern.*;

public class ConcordionExpressionCompletionContributor extends CompletionContributor {

    public ConcordionExpressionCompletionContributor() {
        extend(
                CompletionType.BASIC,
                psiElement(ConcordionTypes.IDENTIFIER)
                        .withParent(ConcordionMember.class)
                        .withLanguage(ConcordionLanguage.INSTANCE),
                new MembersCompletionProvider()
        );
        extend(
                CompletionType.BASIC,
                psiElement(ConcordionTypes.IDENTIFIER)
                        .withParent(ConcordionMember.class)
                        .withSuperParent(PARENT_OF_THE_PARENT, ConcordionOgnlExpressionStart.class)
                        .withLanguage(ConcordionLanguage.INSTANCE),
                new VariablesCompletionProvider()
        );
    }

    private static final class MembersCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            ConcordionMember member = (ConcordionMember) parameters.getPosition().getParent();
//            ConcordionPsiElement parent = member.getConcordionParent();

//            //TODO extract language level from module/project settings
//            PsiClass owner = (parent != null && parent.isArray())
//                    ? JavaPsiFacade.getElementFactory(member.getProject()).getArrayClass(LanguageLevel.JDK_1_8)
//                    : member.getContainingClass();

            result.addAllElements(
                    fromMembersOf(member.getContainingClass()).createAutoCompleteInformation()
            );
        }
    }

    private static final class VariablesCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            PsiFile injectionInSpec = parameters.getOriginalFile();

            result.addAllElements(
                    fromVariablesOf(injectionInSpec).createAutoCompleteInformation()
            );
        }
    }
}
