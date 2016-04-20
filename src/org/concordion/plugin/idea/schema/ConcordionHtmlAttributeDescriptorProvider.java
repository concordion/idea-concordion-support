package org.concordion.plugin.idea.schema;

import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor;
import org.concordion.plugin.idea.ConcordionCommands;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

public class ConcordionHtmlAttributeDescriptorProvider implements XmlAttributeDescriptorsProvider {

    @Override
    public XmlAttributeDescriptor[] getAttributeDescriptors(XmlTag xmlTag) {
        return Stream.of(xmlTag.getAttributes())
                .map(attribute -> getAttributeDescriptor(attribute.getName(), xmlTag))
                .filter(Objects::nonNull)
                .toArray(XmlAttributeDescriptor[]::new);
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(String attributeName, XmlTag xmlTag) {
        String noSchemaPrefix = removeSchemaPrefix(attributeName);

        return ConcordionCommands.DEFAULT_COMMANDS.contains(noSchemaPrefix)
                || ConcordionCommands.EXTENSION_COMMANDS.containsValue(noSchemaPrefix)
                ? new AnyXmlAttributeDescriptor(noSchemaPrefix) : null;
    }

    @NotNull
    private String removeSchemaPrefix(@NotNull String attributeName) {
        int colon = attributeName.indexOf(':');
        return colon != -1 ? attributeName.substring(colon + 1) : attributeName;
    }
}
