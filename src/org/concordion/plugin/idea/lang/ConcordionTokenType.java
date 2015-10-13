package org.concordion.plugin.idea.lang;

import com.intellij.psi.tree.IElementType;

public class ConcordionTokenType extends IElementType {

    public ConcordionTokenType(String debugName) {
        super(debugName, ConcordionLanguage.INSTANCE);
    }
}
