package org.concordion.plugin.idea.marker;

import com.intellij.ide.highlighter.HtmlFileType;
import org.concordion.plugin.idea.ConcordionElementPattern;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static org.concordion.plugin.idea.ConcordionElementPattern.*;
import static org.concordion.plugin.idea.ConcordionPatterns.*;
import static org.concordion.plugin.idea.LineMarker.*;

public class HtmlSpecLineMarkerProvider implements LineMarkerProvider {

    private static final ConcordionElementPattern.Capture<PsiFile> HTML_SPEC = concordionElement(PsiFile.class)
                    .withFileOfType(HtmlFileType.INSTANCE)
                    .withFoundTestFixture();

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        ProcessingContext context = new ProcessingContext();
        if (HTML_SPEC.accepts(element, context)) {

            PsiFile htmlSpec = context.get(SPEC);
            PsiClass testFixture = context.get(TEST_FIXTURE);

            return infoFor(htmlSpec, withNavigationTo(testFixture));
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
