package com.gman.idea.plugin.concordion.autocomplete;

import com.gman.idea.plugin.concordion.Concordion;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class ConcordionCommand extends PatternCondition<PsiElement> {

    public static final ConcordionCommand INSTANCE = new ConcordionCommand();

    public ConcordionCommand() {
        super(ConcordionCommand.class.getSimpleName());
    }

    @Override
    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
        XmlAttribute parent = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
        return parent != null && Concordion.NAMESPACE.equals(parent.getNamespace());
    }
}
