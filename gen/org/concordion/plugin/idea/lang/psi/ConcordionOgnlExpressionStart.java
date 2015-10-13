// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ConcordionOgnlExpressionStart extends PsiElement {

  @Nullable
  ConcordionField getField();

  @NotNull
  List<ConcordionIndex> getIndexList();

  @Nullable
  ConcordionLiteral getLiteral();

  @Nullable
  ConcordionMethod getMethod();

  @Nullable
  ConcordionOgnlExpressionNext getOgnlExpressionNext();

  @Nullable
  ConcordionVariable getVariable();

}
