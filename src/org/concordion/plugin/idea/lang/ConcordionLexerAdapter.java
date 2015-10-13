package org.concordion.plugin.idea.lang;

import com.intellij.lexer.FlexAdapter;

public class ConcordionLexerAdapter extends FlexAdapter {

    public ConcordionLexerAdapter() {
        super(new ConcordionLexer());
    }
}
