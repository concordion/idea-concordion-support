package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.xml.XmlNamespaceHelper;
import org.concordion.plugin.idea.Namespaces;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Collections.singleton;

public class ConcordionCommandInsertionHandler extends XmlAttributeInsertHandler {

    public static final ConcordionCommandInsertionHandler INSTANCE = new ConcordionCommandInsertionHandler(null);

    @Nullable
    private final Namespaces namespaceToAdd;

    public ConcordionCommandInsertionHandler(@Nullable Namespaces namespaceToAdd) {
        this.namespaceToAdd = namespaceToAdd;
    }

    @Override
    public void handleInsert(@NotNull InsertionContext context, @Nullable LookupElement item) {
        super.handleInsert(context, item);

        PsiElement element = context.getFile().findElementAt(context.getStartOffset());
        XmlAttribute concordionCommand = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);

        if (namespaceToAdd != null) {
            commitDocumentFrom(context);
            XmlNamespaceHelper.getHelper(context.getFile()).insertNamespaceDeclaration(
                    (XmlFile) context.getFile(),
                    context.getEditor(),
                    singleton(namespaceToAdd.namespace),
                    namespaceToAdd.defaultPrefix,
                    null
            );
        }

        //This fix is required as sometimes idea does not match already typed schema prefix to completion information
        if (concordionCommand != null && concordionCommand.getLocalName().indexOf(':') != -1) {
            commitDocumentFrom(context);
            concordionCommand.setName(concordionCommand.getLocalName());
        }
    }

    private void commitDocumentFrom(@NotNull InsertionContext context) {
        PsiDocumentManager.getInstance(context.getProject()).commitDocument(context.getDocument());
    }
}
