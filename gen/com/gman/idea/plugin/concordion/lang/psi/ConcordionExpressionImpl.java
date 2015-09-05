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

public class ConcordionExpressionImpl extends ASTWrapperPsiElement implements ConcordionExpression {

  public ConcordionExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitExpression(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ConcordionExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConcordionExpression.class);
  }

  @Override
  @Nullable
  public ConcordionIndex getIndex() {
    return findChildByClass(ConcordionIndex.class);
  }

  @Override
  @Nullable
  public ConcordionLiteral getLiteral() {
    return findChildByClass(ConcordionLiteral.class);
  }

  @Override
  @Nullable
  public ConcordionMethod getMethod() {
    return findChildByClass(ConcordionMethod.class);
  }

  @Override
  @Nullable
  public ConcordionProperty getProperty() {
    return findChildByClass(ConcordionProperty.class);
  }

  @Override
  @Nullable
  public ConcordionVariable getVariable() {
    return findChildByClass(ConcordionVariable.class);
  }

}
