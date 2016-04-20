package org.concordion.plugin.idea.schema;

import com.intellij.javaee.ResourceRegistrar;
import com.intellij.javaee.StandardResourceProvider;
import org.concordion.plugin.idea.Namespaces;

public class ConcordionResourceProvider implements StandardResourceProvider {

    @Override
    public void registerResources(ResourceRegistrar registrar) {
        for (Namespaces namespace : Namespaces.values()) {
            registrar.addIgnoredResource(namespace.namespace);
        }
    }
}
