package org.concordion.plugin.idea;

import org.concordion.plugin.idea.lang.ConcordionIcons;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class LineMarker {

    public static final String CONCORDION_TOOLTIP = "Concordion";

    public static LineMarkerInfo<PsiElement> infoFor(@NotNull PsiElement element, @NotNull GutterIconNavigationHandler<PsiElement> navigationHandler) {
        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                ConcordionIcons.ICON,
                Pass.UPDATE_ALL,
                LineMarker::generateTooltipForConcordion,
                navigationHandler,
                GutterIconRenderer.Alignment.CENTER
        );
    }

    @NotNull
    public static String generateTooltipForConcordion(@NotNull PsiElement element) {
        return CONCORDION_TOOLTIP;
    }

    @NotNull
    public static GutterIconNavigationHandler<PsiElement> withNavigationTo(@NotNull NavigatablePsiElement element) {
        return (e, elt) -> element.navigate(true);
    }
}
