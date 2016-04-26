package org.concordion.plugin.idea;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

import java.util.Set;

public final class ConcordionContextKeys {

    private ConcordionContextKeys() {
    }

    public static final Key<PsiClass> TEST_FIXTURE = new Key<>("CONCORDION_TEST_FIXTURE");
    public static final Key<PsiFile> SPEC = new Key<>("CONCORDION_HTML_SPEC");

    public static final Key<String> CONCORDION_SCHEMA_PREFIX = new Key<>("CONCORDION_SCHEMA_PREFIX");
    public static final Key<String> CONCORDION_EXTENSIONS_SCHEMA_PREFIX = new Key<>("CONCORDION_EXTENSIONS_SCHEMA_PREFIX");
    public static final Key<Set<String>> CONCORDION_EXTENSIONS = new Key<>("CONCORDION_EXTENSIONS");
}
