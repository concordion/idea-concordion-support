package org.concordion.plugin.idea;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static com.intellij.psi.search.ProjectScope.getAllScope;

public final class ConcordionPsiTypeUtils {

    private ConcordionPsiTypeUtils() {
    }

    public static final String STRING = java.lang.String.class.getName();
    public static final String ITERABLE = java.lang.Iterable.class.getCanonicalName();
    public static final String LIST = java.util.List.class.getName();
    public static final String MAP = java.util.Map.class.getName();

    public static PsiType findString(@NotNull Project project) {
        return findType(STRING, project);
    }

    @NotNull
    public static PsiType findIterable(@NotNull Project project) {
        return findType(ITERABLE, project);
    }

    public static boolean isIterable(@NotNull PsiType type, @NotNull Project project) {
        return findIterable(project).isAssignableFrom(type);
    }

    @NotNull
    public static PsiType findList(@NotNull Project project) {
        return findType(LIST, project);
    }

    @NotNull
    public static PsiType findMap(@NotNull Project project) {
        return findType(MAP, project);
    }

    public static boolean isMap(@NotNull PsiType type, @NotNull Project project) {
        return findMap(project).isAssignableFrom(type);
    }

    @NotNull
    public static PsiType findType(@Nullable String qualifiedName, @NotNull Project project) {
        return PsiType.getTypeByName(qualifiedName, project, getAllScope(project));
    }

    @Nullable
    public static PsiType unwrapType(@Nullable PsiType source, int times, @NotNull Function<PsiType, PsiType> transformation) {
        PsiType unwrapped = source;
        for (int i = 0; i < times; i++) {
            if (unwrapped == null) {
                return null;
            }
            unwrapped = transformation.apply(unwrapped);
        }
        return unwrapped;
    }

    @Nullable
    public static PsiType iterableParameterType(@NotNull PsiType listPsiType) {
        return nthGenericTypeFormHierarchy(0, ITERABLE, listPsiType);
    }

    @Nullable
    public static PsiType mapValueParameterType(@NotNull PsiType mapPsiType) {
        return nthGenericTypeFormHierarchy(1, MAP, mapPsiType);
    }

    @Nullable
    private static PsiType nthGenericTypeFormHierarchy(int n, String qualifiedType, @NotNull PsiType currentType) {
        return hierarchy(currentType, new HashSet<>()).stream()
                .filter(st -> st.getCanonicalText().startsWith(qualifiedType))
                .map(t -> nthGeneric(n, t))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    @NotNull
    private static Set<PsiType> hierarchy(@NotNull PsiType currentType, @NotNull Set<PsiType> hierarchy) {
        hierarchy.add(currentType);
        for (PsiType superType : currentType.getSuperTypes()) {
            hierarchy(superType, hierarchy);
        }
        return hierarchy;
    }

    @Nullable
    private static PsiType nthGeneric(int n, @NotNull PsiType owner) {
        if (!(owner instanceof PsiClassType)) {
            return null;
        }
        PsiType[] parameters = ((PsiClassType) owner).getParameters();
        if (parameters.length <= n) {
            return null;
        }
        return parameters[n];
    }
}
