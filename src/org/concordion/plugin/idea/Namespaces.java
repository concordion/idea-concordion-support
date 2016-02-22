package org.concordion.plugin.idea;

import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public enum Namespaces {

    CONCORDION("http://www.concordion.org/2007/concordion", "c"),
    CONCORDION_EXTENSIONS("urn:concordion-extensions:2010", "ext");

    @NotNull public final String namespace;
    @NotNull public final String defaultPrefix;

    Namespaces(@NotNull String namespace, @NotNull String defaultPrefix) {
        this.namespace = namespace;
        this.defaultPrefix = defaultPrefix;
    }

    public static boolean knownNamespace(@NotNull String namespace) {
        return CONCORDION.sameNamespace(namespace) || CONCORDION_EXTENSIONS.sameNamespace(namespace);
    }

    public boolean sameNamespace(@Nullable String namespace) {
        return this.namespace.equalsIgnoreCase(namespace);
    }

    @Nullable
    public String prefixInFile(@NotNull PsiFile psiFile) {
        if (!(psiFile instanceof XmlFile)) {
            return null;
        }
        XmlTag rootTag = ((XmlFile) psiFile).getRootTag();
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
}
