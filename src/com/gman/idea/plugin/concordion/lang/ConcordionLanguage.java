package com.gman.idea.plugin.concordion.lang;

import com.intellij.lang.Language;

public class ConcordionLanguage extends Language {

    public static final ConcordionLanguage INSTANCE = new ConcordionLanguage();

    private ConcordionLanguage() {
        super("Concordion");
    }
}
