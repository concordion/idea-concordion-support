package org.concordion.plugin.idea.settings;


import org.concordion.plugin.idea.*;

public enum ConcordionCommandsCaseType {

    BOTH(true, true),
    CAMEL_CASE(true, false),
    SPINAL_CASE(false, true);

    public final boolean camelCase;
    public final boolean spinalCase;

    ConcordionCommandsCaseType(boolean camelCase, boolean spinalCase) {
        this.camelCase = camelCase;
        this.spinalCase = spinalCase;
    }

    @Override
    public String toString() {
        return ConcordionBundle.message("concordion.settings.case." + name());
    }
}
