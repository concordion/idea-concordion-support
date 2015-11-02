package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;

public class ConcordionCommandInsertionHandler extends XmlAttributeInsertHandler {

    public static final ConcordionCommandInsertionHandler INSTANCE = new ConcordionCommandInsertionHandler();

    @Override
    public void handleInsert(InsertionContext context, LookupElement item) {
        super.handleInsert(context, item);

        PsiElement element = context.getFile().findElementAt(context.getStartOffset());
        XmlAttribute concordionCommand = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);

        //This fix is required as sometimes idea does not match already typed schema prefix to completion information
        if (concordionCommand != null && concordionCommand.getLocalName().indexOf(':') != -1) {
            PsiDocumentManager.getInstance(context.getProject()).commitDocument(context.getDocument());
            concordionCommand.setName(concordionCommand.getLocalName());
        }
    }
}
