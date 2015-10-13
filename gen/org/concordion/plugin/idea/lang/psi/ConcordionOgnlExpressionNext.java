// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ConcordionOgnlExpressionNext extends PsiElement {

  @Nullable
  ConcordionField getField();

  @NotNull
  List<ConcordionIndex> getIndexList();

  @Nullable
  ConcordionMethod getMethod();

  @NotNull
  List<ConcordionOgnlExpressionNext> getOgnlExpressionNextList();

}
