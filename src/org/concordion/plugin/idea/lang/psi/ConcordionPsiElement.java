package org.concordion.plugin.idea.lang.psi;

import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

public interface ConcordionPsiElement extends PsiNamedElement {

    @Nullable PsiType getType();

    boolean isResolvable();
}
