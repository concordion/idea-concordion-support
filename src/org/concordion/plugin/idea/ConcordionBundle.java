package org.concordion.plugin.idea;

import com.intellij.*;
import org.jetbrains.annotations.*;

import java.util.*;

public final class ConcordionBundle {

    private ConcordionBundle() {
    }

    @NotNull
    private static final String BUNDLE_NAME = "org.concordion.plugin.idea.bundle.ConcordionBundle";

    @NotNull
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    @NotNull
    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return CommonBundle.message(BUNDLE, key, params);
    }
}
