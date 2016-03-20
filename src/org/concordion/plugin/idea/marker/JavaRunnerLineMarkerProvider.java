package org.concordion.plugin.idea.marker;

import org.concordion.plugin.idea.patterns.ConcordionElementPattern;
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

import static org.concordion.plugin.idea.ConcordionContextKeys.*;
import static org.concordion.plugin.idea.patterns.ConcordionElementPattern.*;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.*;
import static org.concordion.plugin.idea.marker.LineMarker.*;

public class JavaRunnerLineMarkerProvider implements LineMarkerProvider {

    private static final ConcordionElementPattern.Capture<PsiIdentifier> CLASS_NAME =
            concordionElement(PsiIdentifier.class)
                    .withParent(PsiClass.class)
                    .withSuperParent(PARENT_OF_THE_PARENT, PsiFile.class)
                    .withFoundSpecOfAnyType();

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        ProcessingContext context = new ProcessingContext();
        if (CLASS_NAME.accepts(element, context)) {

            return infoFor(context.get(TEST_FIXTURE));
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
