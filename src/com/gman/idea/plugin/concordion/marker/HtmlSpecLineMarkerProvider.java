package com.gman.idea.plugin.concordion.marker;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.html.HtmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.LineMarker.*;

public class HtmlSpecLineMarkerProvider implements LineMarkerProvider {
    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        if (!(element instanceof HtmlTag)
                || !((HtmlTag) element).getLocalName().equals("html")) {
            return null;
        }

        PsiFile htmlSpec = element.getContainingFile();
        PsiClass runnerClass = correspondingJavaRunner(htmlSpec);

        if (runnerClass == null) {
            return null;
        }

        return infoFor(htmlSpec, withNavigationTo(runnerClass));

    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
