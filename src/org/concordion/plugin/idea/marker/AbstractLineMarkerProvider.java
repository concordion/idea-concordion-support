package org.concordion.plugin.idea.marker;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

public abstract class AbstractLineMarkerProvider implements LineMarkerProvider {

    @NotNull
    private final ConcordionElementPattern.Capture<?> pattern;

    @NotNull
    private final Key<? extends PsiElement> key;

    public AbstractLineMarkerProvider(
            @NotNull ConcordionElementPattern.Capture<?> pattern,
            @NotNull Key<? extends PsiElement> key
    ) {
        this.pattern = pattern;
        this.key = key;
    }

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        ProcessingContext context = new ProcessingContext();
        if (pattern.accepts(element, context)) {
            return concordionFileInfoFor(context.get(key));
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }

    @NotNull
    private static LineMarkerInfo<PsiElement> concordionFileInfoFor(@NotNull PsiElement element) {
        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                ConcordionIcons.ICON,
                AbstractLineMarkerProvider::generateTooltipForConcordionFile,
                AbstractLineMarkerProvider::navigateToPairedFile,
                GutterIconRenderer.Alignment.CENTER
        );
    }

    @NotNull
    private static String generateTooltipForConcordionFile(@NotNull PsiElement element) {
        return ConcordionLanguage.INSTANCE.getDisplayName();
    }

    private static void navigateToPairedFile(@NotNull MouseEvent event, @NotNull PsiElement element) {
        ConcordionNavigationService.getInstance(element.getProject()).navigateToPairedFile(element.getContainingFile());
    }
}
