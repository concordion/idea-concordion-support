package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public class ConcordionElementPattern<T extends PsiElement, Self extends ConcordionElementPattern<T, Self>> extends PsiElementPattern<T, Self> {

    public static final Key<PsiClass> TEST_FIXTURE = new Key<>("CONCORDION_TEST_FIXTURE");
    public static final Key<PsiFile> SPEC = new Key<>("CONCORDION_HTML_SPEC");

    public static final Key<String> CONCORDION_SCHEMA_PREFIX = new Key<>("CONCORDION_SCHEMA_PREFIX");
    public static final Key<String> CONCORDION_EXTENSIONS_SCHEMA_PREFIX = new Key<>("CONCORDION_EXTENSIONS_SCHEMA_PREFIX");
    public static final Key<Collection<String>> CONCORDION_EXTENSIONS = new Key<>("CONCORDION_EXTENSIONS");

    public static final int PARENT_OF_THE_PARENT = 2;

    public ConcordionElementPattern(final Class<T> aClass) {
        super(aClass);
    }

    public Self withFoundTestFixture() {
        return with(new PatternCondition<T>("withFoundTestFixture") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiFile spec = element.getContainingFile();
                if (spec == null || spec.getParent() == null) {
                    spec = ConcordionInjectionUtils.getTopLevelFile(element);
                }
                if (spec == null) {
                    return false;
                }
                PsiClass testFixture = ConcordionNavigationService.getInstance(spec.getProject()).correspondingTestFixture(spec);

                context.put(SPEC, spec);
                context.put(TEST_FIXTURE, testFixture);

                return testFixture != null;
            }
        });
    }

    public Self withFoundSpecOfAnyType() {
        return with(new PatternCondition<T>("withFoundSpecOfAnyType") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiClass testFixture = PsiTreeUtil.getParentOfType(element, PsiClass.class);
                if (testFixture == null) {
                    return false;
                }
                PsiFile spec = ConcordionNavigationService.getInstance(testFixture.getProject()).correspondingSpec(testFixture);

                context.put(SPEC, spec);
                context.put(TEST_FIXTURE, testFixture);

                return spec != null;
            }
        });
    }

    public Self withConcordionHtmlSpec() {
        return with(new PatternCondition<T>("withConcordionHtmlSpec") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                String prefix = Namespaces.CONCORDION.prefixInFile(element.getContainingFile());
                context.put(CONCORDION_SCHEMA_PREFIX, prefix);
                return prefix != null;
            }
        });
    }

    public Self withConcordionMdSpec() {
        throw new UnsupportedOperationException("Unimplemented yet");
    }

    public Self withFullOgnl(boolean isUsingFullOgnl) {
        return with(new PatternCondition<T>("withFullOgnl") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiClass testFixture = context.get(TEST_FIXTURE);
                return testFixture != null && isUsingFullOgnl(testFixture) == isUsingFullOgnl;
            }

            private boolean isUsingFullOgnl(@NotNull PsiClass runnerClass) {
                return findAnnotationInClassHierarchy(runnerClass, CONCORDION_FULL_OGNL) != null;
            }
        });
    }

    public Self withTestFixtureConfigured(boolean configured) {
        return with(new PatternCondition<T>("withJunitRunWithAnnotation") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiClass testFixture = context.get(TEST_FIXTURE);
                return testFixture != null && isConcordionFixture(testFixture) == configured;
            }
        });
    }

    public Self withConfiguredExtensions() {
        return with(new PatternCondition<T>("withConfiguredExtensions") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                String prefix = Namespaces.CONCORDION_EXTENSIONS.prefixInFile(element.getContainingFile());
                Collection<String> extensions = configuredExtensions(context.get(TEST_FIXTURE));

                context.put(CONCORDION_EXTENSIONS_SCHEMA_PREFIX, prefix);
                context.put(CONCORDION_EXTENSIONS, extensions);

                return !extensions.isEmpty();
            }
        });
    }

    public Self withConcordionAttribute() {
        return with(new PatternCondition<T>("withConcordionAttribute") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                XmlAttribute attribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
                return attribute != null && Namespaces.knownNamespace(attribute.getNamespace());
            }
        });
    }

    public Self withConcordionCommand(String... commands) {
        return withConcordionCommand(ImmutableSet.copyOf(commands));
    }

    public Self withConcordionCommand(Set<String> commands) {
        return with(new PatternCondition<T>("withConcordionCommand") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                XmlAttribute attribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
                return attribute != null && commands.contains(attribute.getLocalName());
            }
        });
    }

    public static class Capture<T extends PsiElement> extends ConcordionElementPattern<T, Capture<T>> {

        protected Capture(final Class<T> aClass) {
            super(aClass);
        }
    }
}
