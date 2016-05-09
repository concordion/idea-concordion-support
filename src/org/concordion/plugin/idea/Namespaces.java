package org.concordion.plugin.idea;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static org.concordion.plugin.idea.ConcordionContextKeys.*;

public enum Namespaces {

    CONCORDION("http://www.concordion.org/2007/concordion", "c", CONCORDION_SCHEMA_PREFIX),
    CONCORDION_EXTENSIONS("urn:concordion-extensions:2010", "ext", CONCORDION_EXTENSIONS_SCHEMA_PREFIX);

    @NotNull public final String namespace;
    @NotNull public final String defaultPrefix;
    @NotNull private final Key<String> namespaceKeyInContext;

    Namespaces(@NotNull String namespace, @NotNull String defaultPrefix, @NotNull Key<String> namespaceKeyInContext) {
        this.namespace = namespace;
        this.defaultPrefix = defaultPrefix;
        this.namespaceKeyInContext = namespaceKeyInContext;
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

    @NotNull
    public String computePrefix(@NotNull ProcessingContext context) {
        String prefix = context.get(namespaceKeyInContext);
        return prefix != null ? prefix : defaultPrefix;
    }

    public boolean prefixPrecomputed(@NotNull ProcessingContext context) {
        return context.get(namespaceKeyInContext) != null;
    }
}
