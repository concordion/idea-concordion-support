package com.gman.idea.plugin.concordion.lang;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;

public class ConcordionPsiUtils {

    public static PsiReference[] getReferences(ASTWrapperPsiElement element) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(element);
    }

    public static String getName(ConcordionMethod method) {
        ASTNode identifierNode = method.getNode().findChildByType(ConcordionTypes.IDENTIFIER);
        return identifierNode != null ? identifierNode.getText() : null;
    }

    public static int getParametersCount(ConcordionMethod method) {
        ASTNode arguments = method.getNode().findChildByType(ConcordionTypes.ARGUMENTS);
        return (arguments.getChildren(null).length + 1) / 2;
    }

    public static String getName(ConcordionField field) {
        ASTNode identifierNode = field.getNode().findChildByType(ConcordionTypes.IDENTIFIER);
        return identifierNode != null ? identifierNode.getText() : null;
    }
}
