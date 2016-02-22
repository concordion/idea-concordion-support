package org.concordion.plugin.idea.marker;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.concordion.plugin.idea.ConcordionElementPattern;
import org.concordion.plugin.idea.ConcordionSpecType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.LineMarker.infoFor;
import static org.concordion.plugin.idea.LineMarker.withNavigationTo;
import static org.concordion.plugin.idea.ConcordionContextKeys.*;

public abstract class AbstractSpecLineMarkerProvider implements LineMarkerProvider {

    private final ConcordionElementPattern.Capture<PsiFile> file;

    public AbstractSpecLineMarkerProvider(@NotNull ConcordionSpecType type) {
        this.file = concordionElement(PsiFile.class)
                .withSpecOfType(type)
                .withFoundTestFixture();
    }

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        ProcessingContext context = new ProcessingContext();
        if (file.accepts(element, context)) {

            PsiFile spec = context.get(SPEC);
            PsiClass testFixture = context.get(TEST_FIXTURE);

            return infoFor(spec, withNavigationTo(testFixture));
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}