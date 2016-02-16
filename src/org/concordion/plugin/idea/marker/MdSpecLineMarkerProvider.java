package org.concordion.plugin.idea.marker;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionElementPattern;
import org.intellij.plugins.markdown.lang.MarkdownFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static org.concordion.plugin.idea.ConcordionElementPattern.SPEC;
import static org.concordion.plugin.idea.ConcordionElementPattern.TEST_FIXTURE;
import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.LineMarker.infoFor;
import static org.concordion.plugin.idea.LineMarker.withNavigationTo;

public class MdSpecLineMarkerProvider implements LineMarkerProvider {

    private static final ConcordionElementPattern.Capture<PsiFile> FILE = concordionElement(PsiFile.class)
            .withFileOfType(MarkdownFileType.INSTANCE)
            .withFoundTestFixture();

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        ProcessingContext context = new ProcessingContext();
        if (FILE.accepts(element, context)) {

            PsiFile mdSpec = context.get(SPEC);
            PsiClass testFixture = context.get(TEST_FIXTURE);

            return infoFor(mdSpec, withNavigationTo(testFixture));
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
