package org.concordion.plugin.idea.injection;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConcordionInjection implements PsiLanguageInjectionHost, NavigationItem {

    private final PsiElement delegate;

    public ConcordionInjection(PsiElement delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isValidHost() {
        return true;
    }

    @Override
    public PsiLanguageInjectionHost updateText(@NotNull String text) {
        ASTNode node = delegate.getNode();
        if (node instanceof LeafElement) {
            ((LeafElement) node).replaceWithText(text);
        }
        PsiElement newDelegate = rebuildMarkdownInjectionHolder(text);
        if (newDelegate != null) {
            delegate.replace(newDelegate);
        }
        return this;
    }

    @Nullable
    private PsiElement rebuildMarkdownInjectionHolder(@NotNull String newText) {
        String dummyLinkText = "[dummy](- " + newText + ")";

        PsiFile dummyFile = PsiFileFactory.getInstance(delegate.getProject())
                .createFileFromText("dummy", delegate.getContainingFile().getFileType(), dummyLinkText);

        PsiElement injection = dummyFile.getFirstChild().findElementAt(dummyLinkText.indexOf(newText) + 1);

        return injection != null ? injection.getParent() : null;
    }

    @NotNull
    @Override
    public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
        return new LiteralTextEscaper<ConcordionInjection>(this) {
            @Override
            public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
                outChars.append(this.myHost.getText().substring(rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset()));
                return true;
            }

            @Override
            public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
                return offsetInDecoded + rangeInsideHost.getStartOffset();
            }

            @Override
            public boolean isOneLine() {
                return true;
            }
        };
    }

    @Override
    @NotNull
    @Contract(
        pure = true
    )
    public Project getProject() throws PsiInvalidElementAccessException {
        return delegate.getProject();
    }

    @Override
    @NotNull
    @Contract(
        pure = true
    )
    public Language getLanguage() {
        return delegate.getLanguage();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiManager getManager() {
        return delegate.getManager();
    }

    @Override
    @NotNull
    @Contract(
        pure = true
    )
    public PsiElement[] getChildren() {
        return delegate.getChildren();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiElement getParent() {
        return delegate.getParent();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiElement getFirstChild() {
        return delegate.getFirstChild();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiElement getLastChild() {
        return delegate.getLastChild();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiElement getNextSibling() {
        return delegate.getNextSibling();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiElement getPrevSibling() {
        return delegate.getPrevSibling();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
        return delegate.getContainingFile();
    }

    @Override
    @Contract(
        pure = true
    )
    public TextRange getTextRange() {
        return delegate.getTextRange();
    }

    @Override
    @Contract(
        pure = true
    )
    public int getStartOffsetInParent() {
        return delegate.getStartOffsetInParent();
    }

    @Override
    @Contract(
        pure = true
    )
    public int getTextLength() {
        return delegate.getTextLength();
    }

    @Override
    @Nullable
    @Contract(
        pure = true
    )
    public PsiElement findElementAt(int i) {
        return delegate.findElementAt(i);
    }

    @Override
    @Nullable
    @Contract(
        pure = true
    )
    public PsiReference findReferenceAt(int i) {
        return delegate.findReferenceAt(i);
    }

    @Override
    @Contract(
        pure = true
    )
    public int getTextOffset() {
        return delegate.getTextOffset();
    }

    @Override
    @NonNls
    @Contract(
        pure = true
    )
    public String getText() {
        return delegate.getText();
    }

    @Override
    @NotNull
    @Contract(
        pure = true
    )
    public char[] textToCharArray() {
        return delegate.textToCharArray();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiElement getNavigationElement() {
        return delegate.getNavigationElement();
    }

    @Override
    @Contract(
        pure = true
    )
    public PsiElement getOriginalElement() {
        return delegate.getOriginalElement();
    }

    @Override
    @Contract(
        pure = true
    )
    public boolean textMatches(@NotNull @NonNls CharSequence charSequence) {
        return delegate.textMatches(charSequence);
    }

    @Override
    @Contract(
        pure = true
    )
    public boolean textMatches(@NotNull PsiElement psiElement) {
        return delegate.textMatches(psiElement);
    }

    @Override
    @Contract(
        pure = true
    )
    public boolean textContains(char c) {
        return delegate.textContains(c);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor psiElementVisitor) {
        delegate.accept(psiElementVisitor);
    }

    @Override
    public void acceptChildren(@NotNull PsiElementVisitor psiElementVisitor) {
        delegate.acceptChildren(psiElementVisitor);
    }

    @Override
    public PsiElement copy() {
        return new ConcordionInjection(delegate.copy());
    }

    @Override
    public PsiElement add(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        return delegate.add(psiElement);
    }

    @Override
    public PsiElement addBefore(@NotNull PsiElement psiElement, @Nullable PsiElement psiElement1) throws IncorrectOperationException {
        return delegate.addBefore(psiElement, psiElement1);
    }

    @Override
    public PsiElement addAfter(@NotNull PsiElement psiElement, @Nullable PsiElement psiElement1) throws IncorrectOperationException {
        return delegate.addAfter(psiElement, psiElement1);
    }

    @Override
    public void checkAdd(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        delegate.checkAdd(psiElement);
    }

    @Override
    public PsiElement addRange(PsiElement psiElement, PsiElement psiElement1) throws IncorrectOperationException {
        return delegate.addRange(psiElement, psiElement1);
    }

    @Override
    public PsiElement addRangeBefore(@NotNull PsiElement psiElement, @NotNull PsiElement psiElement1, PsiElement psiElement2) throws IncorrectOperationException {
        return delegate.addRangeBefore(psiElement, psiElement1, psiElement2);
    }

    @Override
    public PsiElement addRangeAfter(PsiElement psiElement, PsiElement psiElement1, PsiElement psiElement2) throws IncorrectOperationException {
        return delegate.addRangeAfter(psiElement, psiElement1, psiElement2);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        delegate.delete();
    }

    @Override
    public void checkDelete() throws IncorrectOperationException {
        delegate.checkDelete();
    }

    @Override
    public void deleteChildRange(PsiElement psiElement, PsiElement psiElement1) throws IncorrectOperationException {
        delegate.deleteChildRange(psiElement, psiElement1);
    }

    @Override
    public PsiElement replace(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        return delegate.replace(psiElement);
    }

    @Override
    @Contract(
        pure = true
    )
    public boolean isValid() {
        return delegate.isValid();
    }

    @Override
    @Contract(
        pure = true
    )
    public boolean isWritable() {
        return delegate.isWritable();
    }

    @Override
    @Nullable
    @Contract(
        pure = true
    )
    public PsiReference getReference() {
        return delegate.getReference();
    }

    @Override
    @NotNull
    @Contract(
        pure = true
    )
    public PsiReference[] getReferences() {
        return delegate.getReferences();
    }

    @Override
    @Nullable
    @Contract(
        pure = true
    )
    public <T> T getCopyableUserData(Key<T> key) {
        return delegate.getCopyableUserData(key);
    }

    @Override
    public <T> void putCopyableUserData(Key<T> key, @Nullable T t) {
        delegate.putCopyableUserData(key, t);
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor psiScopeProcessor, @NotNull ResolveState resolveState, @Nullable PsiElement psiElement, @NotNull PsiElement psiElement1) {
        return delegate.processDeclarations(psiScopeProcessor, resolveState, psiElement, psiElement1);
    }

    @Override
    @Nullable
    @Contract(
        pure = true
    )
    public PsiElement getContext() {
        return delegate.getContext();
    }

    @Override
    @Contract(
        pure = true
    )
    public boolean isPhysical() {
        return !ApplicationManager.getApplication().isUnitTestMode() && delegate.isPhysical();
    }

    @Override
    @NotNull
    @Contract(
        pure = true
    )
    public GlobalSearchScope getResolveScope() {
        return delegate.getResolveScope();
    }

    @Override
    @NotNull
    @Contract(
        pure = true
    )
    public SearchScope getUseScope() {
        return delegate.getUseScope();
    }

    @Override
    @Contract(
        pure = true
    )
    public ASTNode getNode() {
        return delegate.getNode();
    }

    @Override
    @Contract(
        pure = true
    )
    public boolean isEquivalentTo(PsiElement psiElement) {
        return delegate.isEquivalentTo(psiElement);
    }

    @Override
    @Nullable
    public <T> T getUserData(@NotNull Key<T> key) {
        return delegate.getUserData(key);
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {
        delegate.putUserData(key, t);
    }

    @Override
    public Icon getIcon(@IconFlags int i) {
        return delegate.getIcon(i);
    }

    @Nullable
    @Override
    public String getName() {
        return ((NavigationItem) delegate).getName();
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        return ((NavigationItem) delegate).getPresentation();
    }

    @Override
    public void navigate(boolean b) {
        ((NavigationItem) delegate).navigate(b);
    }

    @Override
    public boolean canNavigate() {
        return ((NavigationItem) delegate).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return ((NavigationItem) delegate).canNavigateToSource();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    @NonNls
    @Contract(
            pure = true
    )
    public String toString() {
        return delegate.toString();
    }
}
