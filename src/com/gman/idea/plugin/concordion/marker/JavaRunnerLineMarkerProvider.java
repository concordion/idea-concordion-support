package com.gman.idea.plugin.concordion.marker;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.LineMarker.*;
import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;

public class JavaRunnerLineMarkerProvider implements LineMarkerProvider {
    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        if (!(element instanceof PsiClass)
                || !(element.getParent() instanceof PsiFile)) {
            return null;
        }

        PsiClass javaRunner = (PsiClass) element;
        PsiFile htmlSpec = correspondingHtmlSpec(javaRunner);

        PsiIdentifier className = getChildOfType(element, PsiIdentifier.class);

        if (className == null || htmlSpec == null) {
            return null;
        }

        return infoFor(className, withNavigationTo(htmlSpec));

    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
