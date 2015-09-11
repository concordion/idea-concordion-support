package com.gman.idea.plugin.concordion.action.quickfix;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateFieldFromUsageHelper;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.search.ProjectScope.getAllScope;

public class CreateFieldFromConcordionUsage extends CreateFromConcordionUsage<ConcordionField> {

    public CreateFieldFromConcordionUsage(@Nullable PsiClass javaRunner, @NotNull ConcordionField source) {
        super(javaRunner, source, "Create field from usage");
    }

    @Override
    protected PsiMember createdMember(Project project, PsiElementFactory factory) {
        PsiType defaultFieldType = PsiType.getTypeByName("java.lang.Object", project, getAllScope(project));

        PsiField createdField = factory.createField(soure.getFieldName(), defaultFieldType);
        createdField.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);

        return CreateFieldFromUsageHelper.insertField(javaRunner, createdField, javaRunner);
    }
}
