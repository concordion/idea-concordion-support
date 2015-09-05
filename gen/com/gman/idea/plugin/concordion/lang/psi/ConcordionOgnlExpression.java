// This is a generated file. Not intended for manual editing.
package com.gman.idea.plugin.concordion.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ConcordionOgnlExpression extends PsiElement {

  @Nullable
  ConcordionIndex getIndex();

  @Nullable
  ConcordionLiteral getLiteral();

  @Nullable
  ConcordionMethod getMethod();

  @NotNull
  List<ConcordionOgnlExpression> getOgnlExpressionList();

  @Nullable
  ConcordionProperty getProperty();

  @Nullable
  ConcordionVariable getVariable();

}
