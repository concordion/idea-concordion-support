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

public class ConcordionStatementImpl extends ASTWrapperPsiElement implements ConcordionStatement {

  public ConcordionStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ConcordionEmbeddedCommand getEmbeddedCommand() {
    return findChildByClass(ConcordionEmbeddedCommand.class);
  }

  @Override
  @Nullable
  public ConcordionIterateExpression getIterateExpression() {
    return findChildByClass(ConcordionIterateExpression.class);
  }

  @Override
  @Nullable
  public ConcordionOgnlExpressionStart getOgnlExpressionStart() {
    return findChildByClass(ConcordionOgnlExpressionStart.class);
  }

  @Override
  @Nullable
  public ConcordionSetExpression getSetExpression() {
    return findChildByClass(ConcordionSetExpression.class);
  }

}
