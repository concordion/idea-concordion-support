package org.concordion.plugin.idea.settings;

import org.jetbrains.annotations.NotNull;

public enum ConcordionCommandsCompletionType {

    BOTH(true, true, "CamelCase and spinal-case"),
    CAMEL_CASE(true, false, "spinal-case only"),
    SPINAL_CASE(false, true, "CamelCase only");

    public final boolean completeWithCamelCase;
    public final boolean completeWithSpinalCase;
    @NotNull public final String name;

    ConcordionCommandsCompletionType(boolean completeWithCamelCase, boolean completeWithSpinalCase, @NotNull String name) {
        this.completeWithCamelCase = completeWithCamelCase;
        this.completeWithSpinalCase = completeWithSpinalCase;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
