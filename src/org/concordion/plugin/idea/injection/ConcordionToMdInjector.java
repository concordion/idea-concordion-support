package org.concordion.plugin.idea.injection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.concordion.plugin.idea.lang.ConcordionLanguage;
import org.intellij.plugins.markdown.lang.MarkdownElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ConcordionToMdInjector implements MultiHostInjector {

    private static final Set<IElementType> MD_INJECTION_PLACES = ImmutableSet.of(
            MarkdownElementTypes.LINK_DESTINATION,
            MarkdownElementTypes.LINK_DEFINITION,
            MarkdownElementTypes.LINK_TITLE
    );

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
        ASTWrapperPsiElement host = (ASTWrapperPsiElement) context;
        IElementType hostType = host.getNode().getElementType();

        if (!MD_INJECTION_PLACES.contains(hostType)) {
            return;
        }

        TextRange range = rangeInHost(host);

        if (range == null) {
            return;
        }

        registrar
                .startInjecting(ConcordionLanguage.INSTANCE)
                .addPlace(null, null, new ConcordionInjection(host), range)
                .doneInjecting();
    }

    private TextRange rangeInHost(ASTWrapperPsiElement host) {
        String text = host.getText();

        int eq = text.indexOf('=');
        int quote = text.indexOf('"');

        if (eq == -1 && quote == -1) {
            return null;
        }

        return new TextRange(
                (eq != -1 ? eq : quote) + 1,
                text.length() - 1
        );
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return ImmutableList.of(ASTWrapperPsiElement.class);
    }
}
