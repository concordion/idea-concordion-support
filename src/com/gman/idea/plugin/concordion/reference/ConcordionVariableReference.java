package com.gman.idea.plugin.concordion.reference;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionVariable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import static com.gman.idea.plugin.concordion.Concordion.unpackSpecFromLanguageInjection;
import static java.util.Arrays.asList;

public class ConcordionVariableReference extends AbstractConcordionReference<ConcordionVariable> {

    public ConcordionVariableReference(@NotNull ConcordionVariable owner) {
        super(owner);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        PsiFile htmlSpec = unpackSpecFromLanguageInjection(owner.getContainingFile());
        if (htmlSpec == null) {
            return null;
        }
        String text = htmlSpec.getText();
        String varName = owner.getName();
        if (varName == null) {
            return null;
        }

        for (int pos = text.indexOf(varName);
             pos != -1;
             pos = text.indexOf(varName, pos+1)) {

            PsiElement elementAt = htmlSpec.findElementAt(pos);
            if (elementAt != null && isAssigningCommand(elementAt)) {
                return elementAt.getParent();
            }
        }

        return null;
    }

    private static final Set<String> ASSIGN_COMMANDS = new HashSet<>(asList("set", "execute"));

    private boolean isAssigningCommand(PsiElement element) {
        //token/xml_attribute_value/xml_attribute
        if (element != null
                && element.getParent() != null
                && element.getParent().getParent() instanceof XmlAttribute) {

            XmlAttribute attribute = (XmlAttribute) element.getParent().getParent();
            return ASSIGN_COMMANDS.contains(attribute.getLocalName());
        }
        return false;
    }
}
