// This is a generated file. Not intended for manual editing.
package com.gman.idea.plugin.concordion.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface ConcordionMethod extends PsiElement {

  @NotNull
  ConcordionArguments getArguments();

  PsiReference[] getReferences();

  String getName();

  int getParametersCount();

}
