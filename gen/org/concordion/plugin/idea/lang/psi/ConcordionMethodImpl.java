// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.concordion.plugin.idea.lang.psi.ConcordionTypes.*;

public class ConcordionMethodImpl extends ConcordionMethodInternalImpl implements ConcordionMethod {

  public ConcordionMethodImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ConcordionVisitor visitor) {
    visitor.visitMethod(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) accept((ConcordionVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ConcordionArguments getArguments() {
    return findNotNullChildByClass(ConcordionArguments.class);
  }

}
