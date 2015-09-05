// This is a generated file. Not intended for manual editing.
package com.gman.idea.plugin.concordion.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ConcordionExpression extends PsiElement {

  @NotNull
  List<ConcordionExpression> getExpressionList();

  @Nullable
  ConcordionIndex getIndex();

  @Nullable
  ConcordionLiteral getLiteral();

  @Nullable
  ConcordionMethod getMethod();

  @Nullable
  ConcordionProperty getProperty();

  @Nullable
  ConcordionVariable getVariable();

}
