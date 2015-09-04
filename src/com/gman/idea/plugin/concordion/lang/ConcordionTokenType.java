package com.gman.idea.plugin.concordion.lang;

import com.gman.idea.plugin.concordion.lang.ConcordionLanguage;
import com.intellij.psi.tree.IElementType;

public class ConcordionTokenType extends IElementType {

    public ConcordionTokenType(String debugName) {
        super(debugName, ConcordionLanguage.INSTANCE);
    }
}
