package com.gman.idea.plugin.concordion.lang;

import com.intellij.lexer.FlexAdapter;

public class LexerAdapter extends FlexAdapter {

    public LexerAdapter() {
        super(new ConcordionLexer());
    }
}
