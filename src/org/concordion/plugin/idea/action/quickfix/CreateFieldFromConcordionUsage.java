package org.concordion.plugin.idea.action.quickfix;

import org.concordion.plugin.idea.lang.psi.ConcordionField;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.PsiModifier.PUBLIC;
import static org.concordion.plugin.idea.ConcordionPsiTypeUtils.findObject;

public class CreateFieldFromConcordionUsage extends CreateFromConcordionUsage<ConcordionField> {

    public CreateFieldFromConcordionUsage(@Nullable PsiClass javaRunner, @NotNull ConcordionField source) {
        super(javaRunner, source, "Create field from usage");
    }

    @Override
    protected PsiMember createdMember(Project project, PsiElementFactory factory) {
        PsiType defaultFieldType = findObject(project);

        PsiField createdField = factory.createField(source.getName(), defaultFieldType);
        createdField.getModifierList().setModifierProperty(PUBLIC, true);

        createdField = (PsiField) javaRunner.add(createdField);

        return createdField;
    }
}
