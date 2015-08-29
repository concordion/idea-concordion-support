package com.gman.idea.plugin.concordion;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.xml.XmlTag;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class Concordion {

    public static final String NAMESPACE = "http://www.concordion.org/2007/concordion";
    public static final List<String> COMMANDS = unmodifiableList(asList(
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "echo",
            "execute",
            "run",
            "set",
            "verifyRows", "verify-rows",
            "example"
    ));

    public static boolean isConcordionHtmlSpec(PsiFile psiFile) {
        return HtmlFileType.INSTANCE.equals(psiFile.getFileType())
                && psiFile.getText().contains(NAMESPACE);
    }

    public static String concordionSchemaPrefixOf(HtmlFileImpl psiFile) {
        XmlTag rootTag = psiFile.getRootTag();
        if (rootTag == null) {
            return null;
        }
        for (Map.Entry<String, String> declaration : rootTag.getLocalNamespaceDeclarations().entrySet()) {
            if (NAMESPACE.equalsIgnoreCase(declaration.getValue())) {
                return declaration.getKey();
            }
        }
        return null;
    }

    private Concordion() {
    }
}
