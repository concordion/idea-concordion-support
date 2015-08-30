package com.gman.idea.plugin.concordion;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class ConcordionGutterRenderer {

    public static LineMarkerInfo.LineMarkerGutterIconRenderer<PsiElement> rendererForRunnerClass(PsiClass runnerClass, PsiFile htmlSpec) {
        return new LineMarkerInfo.LineMarkerGutterIconRenderer<>(infoFor(runnerClass, withNavigationTo(htmlSpec)));
    }

    public static LineMarkerInfo.LineMarkerGutterIconRenderer<PsiElement> rendererForHtmlSpec(PsiFile htmlSpec, PsiClass runnerClass) {
        return new LineMarkerInfo.LineMarkerGutterIconRenderer<>(infoFor(htmlSpec, withNavigationTo(runnerClass)));
    }

    private static LineMarkerInfo<PsiElement> infoFor(PsiElement element, GutterIconNavigationHandler<PsiElement> navigationHandler) {
        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                AllIcons.Gutter.Unique,
                Pass.UPDATE_ALL,
                ConcordionGutterRenderer::generateTooltipForConcordion,
                navigationHandler,
                GutterIconRenderer.Alignment.CENTER
        );
    }

    private static String generateTooltipForConcordion(PsiElement element) {
        return "Concordion";
    }

    private static GutterIconNavigationHandler<PsiElement> withNavigationTo(NavigatablePsiElement element) {
        return (e, elt) -> element.navigate(true);
    }
}
