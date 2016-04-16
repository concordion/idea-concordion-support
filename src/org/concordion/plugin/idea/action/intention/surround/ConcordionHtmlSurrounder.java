package org.concordion.plugin.idea.action.intention.surround;

import org.jetbrains.annotations.NotNull;

public class ConcordionHtmlSurrounder implements ConcordionSurrounder {

    @NotNull
    @Override
    public String surround(@NotNull String selection) {
        return "<span c:>" + selection + "</span>";
    }

    @Override
    public int caretOffsetInSurroundedText(@NotNull String surroundedText) {
        return 8;
    }
}
