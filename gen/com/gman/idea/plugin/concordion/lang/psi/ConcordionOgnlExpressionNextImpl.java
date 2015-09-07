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

public class ConcordionOgnlExpressionNextImpl extends ASTWrapperPsiElement implements ConcordionOgnlExpressionNext {

  public ConcordionOgnlExpressionNextImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitOgnlExpressionNext(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ConcordionField getField() {
    return findChildByClass(ConcordionField.class);
  }

  @Override
  @Nullable
  public ConcordionIndex getIndex() {
    return findChildByClass(ConcordionIndex.class);
  }

  @Override
  @Nullable
  public ConcordionMethod getMethod() {
    return findChildByClass(ConcordionMethod.class);
  }

  @Override
  @NotNull
  public List<ConcordionOgnlExpressionNext> getOgnlExpressionNextList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConcordionOgnlExpressionNext.class);
  }

}
