package com.gman.idea.plugin.concordion.marker;

import com.gman.idea.plugin.concordion.ConcordionElementPattern;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static com.gman.idea.plugin.concordion.ConcordionElementPattern.*;
import static com.gman.idea.plugin.concordion.ConcordionPatterns.*;
import static com.gman.idea.plugin.concordion.LineMarker.*;

public class JavaRunnerLineMarkerProvider implements LineMarkerProvider {

    private static final ConcordionElementPattern.Capture<PsiIdentifier> CLASS_NAME =
            concordionElement(PsiIdentifier.class).withParent(PsiClass.class).withSuperParent(2, PsiFile.class).withFoundHtmlSpec();

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        ProcessingContext context = new ProcessingContext();
        if (CLASS_NAME.accepts(element, context)) {

            PsiFile htmlSpec = context.get(HTML_SPEC);
            PsiClass testFixture = context.get(TEST_FIXTURE);

            return infoFor(testFixture, withNavigationTo(htmlSpec));
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
