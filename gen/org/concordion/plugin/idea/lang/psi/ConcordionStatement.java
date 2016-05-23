// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ConcordionStatement extends PsiElement {

  @Nullable
  ConcordionEmbeddedCommand getEmbeddedCommand();

  @Nullable
  ConcordionIterateExpression getIterateExpression();

  @Nullable
  ConcordionOgnlExpressionStart getOgnlExpressionStart();

  @Nullable
  ConcordionSetExpression getSetExpression();

}
