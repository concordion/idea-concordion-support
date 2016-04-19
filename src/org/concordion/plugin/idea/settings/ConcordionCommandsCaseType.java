package org.concordion.plugin.idea.settings;

import org.jetbrains.annotations.NotNull;

public enum ConcordionCommandsCaseType {

    BOTH(true, true, "CamelCase and spinal-case"),
    CAMEL_CASE(true, false, "CamelCase only"),
    SPINAL_CASE(false, true, "spinal-case only");

    public final boolean camelCase;
    public final boolean spinalCase;
    @NotNull public final String name;

    ConcordionCommandsCaseType(boolean camelCase, boolean spinalCase, @NotNull String name) {
        this.camelCase = camelCase;
        this.spinalCase = spinalCase;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
