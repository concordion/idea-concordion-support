// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.concordion.plugin.idea.lang.psi.ConcordionTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;

public class ConcordionOgnlExpressionStartImpl extends ASTWrapperPsiElement implements ConcordionOgnlExpressionStart {

  public ConcordionOgnlExpressionStartImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitOgnlExpressionStart(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ConcordionField getField() {
    return findChildByClass(ConcordionField.class);
  }

  @Override
  @NotNull
  public List<ConcordionIndex> getIndexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConcordionIndex.class);
  }

  @Override
  @Nullable
  public ConcordionList getList() {
    return findChildByClass(ConcordionList.class);
  }

  @Override
  @Nullable
  public ConcordionLiteral getLiteral() {
    return findChildByClass(ConcordionLiteral.class);
  }

  @Override
  @Nullable
  public ConcordionMap getMap() {
    return findChildByClass(ConcordionMap.class);
  }

  @Override
  @Nullable
  public ConcordionMethod getMethod() {
    return findChildByClass(ConcordionMethod.class);
  }

  @Override
  @Nullable
  public ConcordionOgnlExpressionNext getOgnlExpressionNext() {
    return findChildByClass(ConcordionOgnlExpressionNext.class);
  }

  @Override
  @Nullable
  public ConcordionVariable getVariable() {
    return findChildByClass(ConcordionVariable.class);
  }

}
