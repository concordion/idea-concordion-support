// This is a generated file. Not intended for manual editing.
package com.gman.idea.plugin.concordion.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.gman.idea.plugin.concordion.lang.ConcordionPsiUtils;

public class ConcordionConcordionIterateExpressionImpl extends ASTWrapperPsiElement implements ConcordionConcordionIterateExpression {

  public ConcordionConcordionIterateExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitConcordionIterateExpression(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ConcordionOgnlExpressionStart getOgnlExpressionStart() {
    return findNotNullChildByClass(ConcordionOgnlExpressionStart.class);
  }

  @Override
  @NotNull
  public ConcordionVariable getVariable() {
    return findNotNullChildByClass(ConcordionVariable.class);
  }

}
