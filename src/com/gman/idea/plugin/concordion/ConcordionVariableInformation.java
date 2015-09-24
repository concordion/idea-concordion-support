package com.gman.idea.plugin.concordion;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionVariableInternal;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gman.idea.plugin.concordion.Concordion.unpackSpecFromLanguageInjection;
import static com.intellij.psi.search.ProjectScope.getAllScope;
import static java.util.Arrays.asList;

public class ConcordionVariableInformation {

    @NotNull
    private final ConcordionVariableInternal variable;

    private ConcordionVariableInformation(@NotNull ConcordionVariableInternal variable) {
        this.variable = variable;
    }

    @NotNull
    public static ConcordionVariableInformation forVariable(@NotNull ConcordionVariableInternal variable) {
        return new ConcordionVariableInformation(variable);
    }

    public static List<String> declaredVariables() {
        return asList("var1", "var2", "var3");
    }

    public boolean isDeclared() {
        return findDeclaration() != null;
    }

    @Nullable
    public PsiElement findDeclaration() {
        PsiFile htmlSpec = unpackSpecFromLanguageInjection(variable.getContainingFile());
        if (htmlSpec == null) {
            return null;
        }
        String text = htmlSpec.getText();
        String varName = variable.getName();
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

    @NotNull
    public PsiType determineType() {
        Project project = variable.getProject();
        GlobalSearchScope scope = getAllScope(project);

        return PsiType.getTypeByName("java.lang.String", project, scope);
    }

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

    private static final Set<String> ASSIGN_COMMANDS = new HashSet<>(asList("set", "execute"));
}
