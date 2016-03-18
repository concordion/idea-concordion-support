package org.concordion.plugin.idea;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.concordion.plugin.idea.ConcordionSpecType.*;

public class ConcordionSpecTypeStrategy<T> {

    @NotNull
    private final Map<String, T> strategies = new HashMap<>();

    @NotNull
    public ConcordionSpecTypeStrategy<T> add(@NotNull ConcordionSpecType type, @NotNull T instance) {
        for (String extension : type.extensions) {
            strategies.put(extension, instance);
        }
        return this;
    }

    @Nullable
    public T forSpecIn(@NotNull PsiFile file) {
        return strategies.get(file.getFileType().getDefaultExtension());
    }

    @NotNull
    public static <E> ConcordionSpecTypeStrategy<E> create(@NotNull E htmlStrategy, @NotNull E mdStrategy) {
        return new ConcordionSpecTypeStrategy<E>().add(HTML, htmlStrategy).add(MD, mdStrategy);
    }
}
