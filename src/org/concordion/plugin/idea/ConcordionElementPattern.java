package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableSet;
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
import static org.concordion.plugin.idea.ConcordionContextKeys.*;

public class ConcordionElementPattern<T extends PsiElement, Self extends ConcordionElementPattern<T, Self>> extends PsiElementPattern<T, Self> {

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

    public Self withSpecOfType(@NotNull ConcordionSpecType type) {
        return with(new PatternCondition<T>("withSpecOfType") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                return type.canBeIn(element.getContainingFile());
            }
        });
    }

    public Self withConfiguredSpecOfType(@NotNull ConcordionSpecType type) {
        return with(new PatternCondition<T>("withConfiguredSpecOfType") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                return type.configuredIn(element.getContainingFile(), context);
            }
        });
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
                ConcordionSpecType type = ConcordionSpecType.inFile(element.getContainingFile());
                boolean extensionsConfigured = type != null && type.extensionsConfiguredIn(element.getContainingFile(), context);

                Collection<String> extensions = configuredExtensions(context.get(TEST_FIXTURE));
                context.put(CONCORDION_EXTENSIONS, extensions);
                return extensionsConfigured || !extensions.isEmpty();
            }
        });
    }

    public Self withConcordionXmlAttribute() {
        return with(new PatternCondition<T>("withConcordionXmlAttribute") {
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
