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

public class ConcordionConcordionExpressionImpl extends ASTWrapperPsiElement implements ConcordionConcordionExpression {

  public ConcordionConcordionExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitConcordionExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ConcordionConcordionIterateExpression getConcordionIterateExpression() {
    return findChildByClass(ConcordionConcordionIterateExpression.class);
  }

  @Override
  @Nullable
  public ConcordionConcordionSetExpression getConcordionSetExpression() {
    return findChildByClass(ConcordionConcordionSetExpression.class);
  }

  @Override
  @Nullable
  public ConcordionOgnlExpression getOgnlExpression() {
    return findChildByClass(ConcordionOgnlExpression.class);
  }

}
