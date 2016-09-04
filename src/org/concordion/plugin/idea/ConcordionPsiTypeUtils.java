package org.concordion.plugin.idea;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.intellij.psi.search.ProjectScope.getAllScope;

public final class ConcordionPsiTypeUtils {

    private ConcordionPsiTypeUtils() {
    }

    public static final String OBJECT = java.lang.Object.class.getCanonicalName();
    public static final String CLASS = java.lang.Class.class.getCanonicalName();
    public static final String STRING = java.lang.String.class.getCanonicalName();
    public static final String ITERABLE = java.lang.Iterable.class.getCanonicalName();
    public static final String LIST = java.util.List.class.getCanonicalName();
    public static final String MAP = java.util.Map.class.getCanonicalName();

    @NotNull
    public static PsiType findObject(@NotNull Project project) {
        return findType(OBJECT, project);
    }

    @NotNull
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

    @NotNull
    public static PsiType unwrapType(@NotNull PsiType source, int times, @NotNull Function<PsiType, PsiType> transformation) {
        PsiType unwrapped = source;
        for (int i = 0; i < times; i++) {
            unwrapped = transformation.apply(unwrapped);
        }
        return unwrapped;
    }

    @NotNull
    public static PsiType iterableParameterType(@NotNull PsiType listPsiType, @NotNull Project project) {
        return nthGenericTypeFormHierarchy(0, ITERABLE, listPsiType).orElseGet(() -> findObject(project));
    }

    @NotNull
    public static PsiType mapValueParameterType(@NotNull PsiType mapPsiType, @NotNull Project project) {
        return nthGenericTypeFormHierarchy(1, MAP, mapPsiType).orElseGet(() -> findObject(project));
    }

    public static boolean isClassType(@NotNull PsiType type) {
        return type.getCanonicalText().startsWith(CLASS);
    }

    @NotNull
    public static PsiType classParameterType(@NotNull PsiType classPsiType, @NotNull Project project) {
        return nthGenericTypeFormHierarchy(0, CLASS, classPsiType).orElseGet(() -> findObject(project));
    }

    public static boolean isDummyArrayType(@NotNull PsiType type) {
        return type.getCanonicalText().startsWith("_Dummy_.__Array__");
    }

    @NotNull
    public static PsiType dummyArrayParameterType(@NotNull PsiType dummyArrayType) {
        return nthGeneric(0, dummyArrayType);
    }

    @NotNull
    private static Optional<PsiType> nthGenericTypeFormHierarchy(int n, String qualifiedType, @NotNull PsiType currentType) {
        return hierarchy(currentType)
                .filter(st -> st.getCanonicalText().startsWith(qualifiedType))
                .map(t -> nthGeneric(n, t))
                .filter(Objects::nonNull)
                .findFirst();
    }

    @NotNull
    private static Stream<PsiType> hierarchy(@NotNull PsiType type) {
        return hierarchy(type, new HashSet<>()).stream();
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
