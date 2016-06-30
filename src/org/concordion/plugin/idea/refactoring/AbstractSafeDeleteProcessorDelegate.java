package org.concordion.plugin.idea.refactoring;

import com.google.common.collect.ImmutableList;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.safeDelete.NonCodeUsageSearchInfo;
import com.intellij.refactoring.safeDelete.SafeDeleteProcessorDelegate;
import com.intellij.usageView.UsageInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public abstract class AbstractSafeDeleteProcessorDelegate implements SafeDeleteProcessorDelegate {

    @Nullable
    @Override
    public NonCodeUsageSearchInfo findUsages(@NotNull PsiElement element, @NotNull PsiElement[] allElementsToDelete, @NotNull List<UsageInfo> result) {
        return null;
    }

    @Nullable
    @Override
    public Collection<? extends PsiElement> getElementsToSearch(@NotNull PsiElement element, @NotNull Collection<PsiElement> allElementsToDelete) {
        return ImmutableList.of();
    }

    @Nullable
    @Override
    public Collection<String> findConflicts(@NotNull PsiElement element, @NotNull PsiElement[] allElementsToDelete) {
        return ImmutableList.of();
    }

    @Nullable
    @Override
    public UsageInfo[] preprocessUsages(Project project, UsageInfo[] usages) {
        return new UsageInfo[0];
    }

    @Override
    public void prepareForDeletion(PsiElement element) {

    }

    @Override
    public boolean isToSearchInComments(final PsiElement element) {
        return false;
    }

    @Override
    public void setToSearchInComments(final PsiElement element, boolean enabled) {

    }

    @Override
    public boolean isToSearchForTextOccurrences(final PsiElement element) {
        return false;
    }

    @Override
    public void setToSearchForTextOccurrences(final PsiElement element, boolean enabled) {

    }
}
