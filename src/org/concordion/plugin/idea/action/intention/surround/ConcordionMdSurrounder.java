package org.concordion.plugin.idea.action.intention.surround;

import org.jetbrains.annotations.NotNull;

public class ConcordionMdSurrounder implements ConcordionSurrounder {

    @NotNull
    public static final ConcordionSurrounder INSTANCE = new ConcordionMdSurrounder();

    @NotNull
    @Override
    public String surround(@NotNull String selection) {
        return "[" + selection + "](- \"\")";
    }

    @Override
    public int caretOffsetInSurroundedText(@NotNull String surroundedText) {
        return surroundedText.length() - 2;
    }
}
