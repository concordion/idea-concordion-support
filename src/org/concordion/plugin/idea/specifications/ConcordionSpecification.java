package org.concordion.plugin.idea.specifications;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.ConcordionExtension;
import org.concordion.plugin.idea.surround.ConcordionSurrounder;
import org.concordion.plugin.idea.variables.ConcordionVariableUsageSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConcordionSpecification extends ConcordionExtension {

    @NotNull
    ExtensionPointName<ConcordionSpecification> EP_NAME = ExtensionPointName.create("org.concordion.plugin.idea.lang.specification");

    @Nullable
    String prefix(@NotNull PsiFile spec);

    @Nullable
    String extensionPrefix(@NotNull PsiFile spec);

    @NotNull
    ConcordionSurrounder surrounder();

    @NotNull
    ConcordionVariableUsageSearcher variablesUsageSearcher();
}
