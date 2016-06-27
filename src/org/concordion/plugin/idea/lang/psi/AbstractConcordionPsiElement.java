package org.concordion.plugin.idea.lang.psi;

import com.intellij.openapi.project.Project;
import com.intellij.pom.java.LanguageLevel;
import org.concordion.plugin.idea.lang.ConcordionElementFactory;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.arrayDimensionsUsed;

public abstract class AbstractConcordionPsiElement extends ASTWrapperPsiElement implements ConcordionPsiElement {

    public AbstractConcordionPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        ASTNode identifierNode = getNode().findChildByType(ConcordionTypes.IDENTIFIER);
        return identifierNode != null ? identifierNode.getText() : null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        ASTNode identifier = getNode().findChildByType(ConcordionTypes.IDENTIFIER);
        if (identifier != null) {
            getNode().replaceChild(identifier, ConcordionElementFactory.createIdentifier(getProject(), name).getNode());
        }
        return this;
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }

    @Nullable
    public ConcordionPsiElement getConcordionParent() {
        //Parent may not be present for some malformed chains
        if (getParent() == null || getParent().getParent() == null) {
            return null;
        }
        PsiElement parent = getParent().getParent().getFirstChild();
        if (!(parent instanceof ConcordionPsiElement)) {
            return null;
        }
        return (ConcordionPsiElement) parent;
    }

    @Override
    public PsiType getType() {
        return expressionType();
    }

    @Override
    public int usedBrackets() {
        return arrayDimensionsUsed(this);
    }

    @Nullable
    protected abstract PsiType containingType();

    @Nullable
    private PsiType expressionType() {

        Project project = getProject();
        PsiType type = containingType();
        if (type == null) {
            return null;
        }

        if (isDummyArrayType(type)) {
            type = dummyArrayParameterType(type);
        }

        int usedBrackets = usedBrackets();
        int arrayDimensions = type.getArrayDimensions();

        if (arrayDimensions > 0) {
            if (usedBrackets == arrayDimensions) {
                return type.getDeepComponentType();
            } else if (usedBrackets < arrayDimensions) {
                //array type
                return JavaPsiFacade.getElementFactory(project).getArrayClassType(type, LanguageLevel.JDK_1_8);
            } else {
                //too much [] used
                return null;
            }
        }

        if (usedBrackets > 0 && isIterable(type, project)) {
            return unwrapType(type, usedBrackets, generic -> iterableParameterType(generic, project));
        }

        if (usedBrackets > 0 && isMap(type, project)) {
            return unwrapType(type, usedBrackets, generic -> mapValueParameterType(generic, project));
        }

        return type;
    }
}
