package org.concordion.plugin.idea.lang.psi;

import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.*;
import org.concordion.plugin.idea.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;
import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;

public abstract class AbstractConcordionMember extends AbstractConcordionPsiElement implements ConcordionMember {

    protected PsiElementCache<PsiClass> containingClass = new PsiElementCache<>(PsiClass::getQualifiedName);
    protected PsiElementCache<PsiMember> containingMember = new PsiElementCache<>(ConcordionPsiUtils::memberIdentity);

    public AbstractConcordionMember(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiClass getContainingClass() {
        return containingClass.getOrCompute(nullToEmpty(getName()), this::determineContainingClass);
    }

    @Override
    public PsiMember getContainingMember() {
        return containingMember.getOrCompute(nullToEmpty(getName()), this::determineContainingMember);
    }

    @Nullable
    protected PsiClass determineContainingClass() {
        if (getParent() instanceof ConcordionOgnlExpressionStart) {
            PsiFile htmlRunner = getTopLevelFile(this);
            return ConcordionNavigationService.getInstance(getProject()).correspondingJavaRunner(htmlRunner);
        } else {
            ConcordionPsiElement parent = getConcordionParent();
            if (parent == null) {
                return null;
            }
            PsiType parentType = parent.getType();
            if (parentType == null) {
                return null;
            }
            return PsiUtil.resolveClassInType(deriveType(parent, parentType));
        }
    }

    @Nullable
    private PsiType deriveType(@NotNull ConcordionPsiElement parent, @NotNull PsiType parentType) {
        int arrayDimensions = parentType.getArrayDimensions();
        if (arrayDimensions > 0) {
            int usedBrackets = parent.usedBrackets();

            if (usedBrackets == arrayDimensions) {
                return parentType;
            } else if (usedBrackets < arrayDimensions) {
                //array type
                return JavaPsiFacade.getElementFactory(getProject()).getArrayClassType(parentType, LanguageLevel.JDK_1_8);
            } else {
                //too much [] used
                return null;
            }
        }

        if (isIterable(parentType, getProject())) {
            return unwrapType(parentType, parent.usedBrackets(), ConcordionPsiTypeUtils::iterableParameterType);
        }

        if (isMap(parentType, getProject())) {
            return unwrapType(parentType, parent.usedBrackets(), ConcordionPsiTypeUtils::mapValueParameterType);
        }

        return parentType;
    }

    @Nullable
    protected abstract PsiMember determineContainingMember();
}
