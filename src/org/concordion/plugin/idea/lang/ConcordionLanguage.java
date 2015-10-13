package org.concordion.plugin.idea.lang;

import com.intellij.lang.Language;

public class ConcordionLanguage extends Language {

    public static final ConcordionLanguage INSTANCE = new ConcordionLanguage();

    private ConcordionLanguage() {
        super("Concordion");
    }
}
