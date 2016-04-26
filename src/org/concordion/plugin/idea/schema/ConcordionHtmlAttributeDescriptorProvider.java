package org.concordion.plugin.idea.schema;

import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.ConcordionSpecType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.concordion.plugin.idea.ConcordionCommand.commands;

public class ConcordionHtmlAttributeDescriptorProvider implements XmlAttributeDescriptorsProvider {

    private final Set<String> allConcordionCommands = commands()
            .filter(command -> command.fitsSpecType(ConcordionSpecType.HTML))
            .map(ConcordionCommand::text)
            .collect(toSet());

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
        return allConcordionCommands.contains(noSchemaPrefix) ? new AnyXmlAttributeDescriptor(noSchemaPrefix) : null;
    }

    @NotNull
    private String removeSchemaPrefix(@NotNull String attributeName) {
        int colon = attributeName.indexOf(':');
        return colon != -1 ? attributeName.substring(colon + 1) : attributeName;
    }
}
