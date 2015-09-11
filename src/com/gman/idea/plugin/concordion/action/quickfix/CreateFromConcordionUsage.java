package com.gman.idea.plugin.concordion.action.quickfix;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.ide.util.EditSourceUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CreateFromConcordionUsage<T extends PsiElement> extends BaseIntentionAction {

    @Nullable protected final PsiClass javaRunner;
    @NotNull protected final T soure;

    public CreateFromConcordionUsage(@Nullable PsiClass javaRunner, @NotNull T source, String text) {
        this.javaRunner = javaRunner;
        this.soure = source;
        setText(text);
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Create from usage";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return javaRunner != null;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {

        PsiElement created = createdMember(project, JavaPsiFacade.getElementFactory(project));

        Navigatable descriptor = EditSourceUtil.getDescriptor(created);
        if (descriptor != null) {
            descriptor.navigate(true);
        }
    }

    protected abstract PsiMember createdMember(Project project, PsiElementFactory factory);
}
