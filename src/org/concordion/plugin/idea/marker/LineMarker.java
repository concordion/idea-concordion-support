package org.concordion.plugin.idea.marker;

import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;

public class LineMarker {

    public static final String CONCORDION_TOOLTIP = "Concordion";

    public static LineMarkerInfo<PsiElement> infoFor(@NotNull PsiElement element) {
        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                ConcordionIcons.ICON,
                Pass.UPDATE_ALL,
                LineMarker::generateTooltipForConcordion,
                LineMarker::navigateToPairedFile,
                GutterIconRenderer.Alignment.CENTER
        );
    }

    @NotNull
    private static String generateTooltipForConcordion(@NotNull PsiElement element) {
        return CONCORDION_TOOLTIP;
    }

    private static void navigateToPairedFile(@NotNull MouseEvent event, @NotNull PsiElement element) {
        PsiFile pairedFile = ConcordionNavigationService.getInstance(element.getProject()).correspondingPairedFile(element.getContainingFile());
        if (pairedFile != null && pairedFile.canNavigate()) {
            pairedFile.navigate(true);
        }
    }
}
