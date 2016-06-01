package org.concordion.plugin.idea.action.quickfix.factories;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MemberCreationParameters {

    @NotNull public final PsiClass fixture;
    @NotNull public NameAndType member = new NameAndType("", PsiType.NULL);
    @NotNull public List<NameAndType> nameAndTypes = new ArrayList<>();

    @NotNull
    public static MemberCreationParameters memberIn(@NotNull PsiClass fixture) {
        return new MemberCreationParameters(fixture);
    }

    private MemberCreationParameters(@NotNull PsiClass fixture) {
        this.fixture = fixture;
    }

    @NotNull
    public MemberCreationParameters withSignature(@NotNull String name, @NotNull PsiType type) {
        this.member = new NameAndType(name, type);
        return this;
    }

    @NotNull
    public MemberCreationParameters withParameter(@NotNull String name, @NotNull PsiType type) {
        this.nameAndTypes.add(new NameAndType(name, type));
        return this;
    }

    @NotNull
    public Project project() {
        return fixture.getProject();
    }

    public static final class NameAndType {
        @NotNull public final String name;
        @NotNull public final PsiType type;

        public NameAndType(@NotNull String name, @NotNull PsiType type) {
            this.name = name;
            this.type = type;
        }
    }
}
