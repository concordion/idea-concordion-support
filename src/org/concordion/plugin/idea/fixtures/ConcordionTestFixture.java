package org.concordion.plugin.idea.fixtures;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.JVMElementFactory;
import com.intellij.psi.PsiClass;
import org.concordion.plugin.idea.ConcordionExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ConcordionTestFixture extends ConcordionExtension {

    @NotNull
    ExtensionPointName<ConcordionTestFixture> EP_NAME = ExtensionPointName.create("org.concordion.plugin.idea.lang.testFixture");

    @NotNull
    String JUNIT_RUN_WITH_ANNOTATION = "org.junit.runner.RunWith";

    @NotNull
    String CONCORDION_RUNNER = "org.concordion.integration.junit4.ConcordionRunner";

    @NotNull
    String CONCORDION_FULL_OGNL_ANNOTATION = "org.concordion.api.FullOGNL";

    @NotNull
    String CONCORDION_EXTENSIONS_ANNOTATION = "org.concordion.api.extension.Extensions";

    @NotNull
    String CONCORDION_OPTIONS_ANNOTATION = "org.concordion.api.option.ConcordionOptions";

    boolean isConcordionFixture(@NotNull PsiClass testFixture);

    boolean fullOgnlEnabled(@NotNull PsiClass testFixture);

    @NotNull
    Set<String> configuredExtensions(@NotNull PsiClass testFixture);

    @Nullable
    String extensionNamespace(@NotNull PsiClass testFixture);

    @NotNull
    JVMElementFactory elementFactory(@NotNull PsiClass testFixture);
}
