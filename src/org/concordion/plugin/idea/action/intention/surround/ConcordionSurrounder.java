package org.concordion.plugin.idea.action.intention.surround;

import org.jetbrains.annotations.NotNull;

public interface ConcordionSurrounder {

    @NotNull
    String surround(@NotNull String selection);

    int caretOffsetInSurroundedText(@NotNull String surroundedText);
}
