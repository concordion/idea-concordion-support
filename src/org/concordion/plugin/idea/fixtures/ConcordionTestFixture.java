package org.concordion.plugin.idea.fixtures;

import com.intellij.openapi.extensions.ExtensionPointName;
import org.concordion.plugin.idea.ConcordionExtension;
import org.jetbrains.annotations.NotNull;

public interface ConcordionTestFixture extends ConcordionExtension {

    @NotNull
    ExtensionPointName<ConcordionTestFixture> EP_NAME = ExtensionPointName.create("org.concordion.plugin.idea.lang.testFixture");
}
