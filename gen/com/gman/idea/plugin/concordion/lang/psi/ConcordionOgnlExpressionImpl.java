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

public class ConcordionOgnlExpressionImpl extends ASTWrapperPsiElement implements ConcordionOgnlExpression {

  public ConcordionOgnlExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitOgnlExpression(this);
    else super.accept(visitor);
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
  @NotNull
  public List<ConcordionOgnlExpression> getOgnlExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConcordionOgnlExpression.class);
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
