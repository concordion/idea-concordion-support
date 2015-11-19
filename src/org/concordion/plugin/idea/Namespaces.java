package org.concordion.plugin.idea;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Namespaces {

    public static final Namespaces CONCORDION = new Namespaces("http://www.concordion.org/2007/concordion", "c");
    public static final Namespaces CONCORDION_EXTENSIONS = new Namespaces("urn:concordion-extensions:2010", "ext");

    @NotNull private final String namespace;
    @NotNull private final String defaultPrefix;

    private Namespaces(@NotNull String namespace, @NotNull String defaultPrefix) {
        this.namespace = namespace;
        this.defaultPrefix = defaultPrefix;
    }

    public static boolean knownNamespace(@NotNull String namespace) {
        return CONCORDION.sameNamespace(namespace) || CONCORDION_EXTENSIONS.sameNamespace(namespace);
    }

    public String defaultPrefix() {
        return defaultPrefix;
    }

    public boolean sameNamespace(@Nullable String namespace) {
        return this.namespace.equalsIgnoreCase(namespace);
    }

    public String prefixInFile(@NotNull PsiFile psiFile) {
        if (!HtmlFileType.INSTANCE.equals(psiFile.getFileType())) {
            return null;
        }
        XmlTag rootTag = ((HtmlFileImpl) psiFile).getRootTag();
        if (rootTag == null) {
            return null;
        }
        for (Map.Entry<String, String> declaration : rootTag.getLocalNamespaceDeclarations().entrySet()) {
            if (namespace.equalsIgnoreCase(declaration.getValue())) {
                return declaration.getKey();
            }
        }
        return null;
    }


    public boolean configuredInFile(@NotNull PsiFile psiFile) {
        return prefixInFile(psiFile) != null;
    }
}
