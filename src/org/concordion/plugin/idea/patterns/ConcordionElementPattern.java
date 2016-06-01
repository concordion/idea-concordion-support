package org.concordion.plugin.idea.patterns;

import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.concordion.internal.MultiPattern;
import org.concordion.plugin.idea.ConcordionCommand;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.concordion.plugin.idea.Namespaces;
import org.concordion.plugin.idea.lang.psi.ConcordionPsiElement;
import org.concordion.plugin.idea.specifications.ConcordionSpecification;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

import static org.concordion.plugin.idea.ConcordionInjectionUtils.*;
import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.*;
import static org.concordion.plugin.idea.ConcordionContextKeys.*;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.*;

public class ConcordionElementPattern<T extends PsiElement, Self extends ConcordionElementPattern<T, Self>> extends PsiElementPattern<T, Self> {

    public static final int PARENT_OF_THE_PARENT = 2;
    public static final int PARENT_OF_THE_PARENT_OF_THE_PARENT = 3;

    public ConcordionElementPattern(final Class<T> aClass) {
        super(aClass);
    }

    public Self withFoundTestFixture() {
        return with(new PatternCondition<T>("withFoundTestFixture") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiFile spec = element.getContainingFile();
                if (spec == null || spec.getParent() == null) {
                    spec = getTopLevelFile(element);
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

    public Self withSpecOfType(@NotNull ConcordionSpecification type) {
        return with(new PatternCondition<T>("withSpecOfType") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiFile file = getContainingFile(element);
                return file != null && type.usableWith(file);
            }
        });
    }

    public Self withConfiguredSpecOfType(@NotNull ConcordionSpecification type) {
        return with(new PatternCondition<T>("withConfiguredSpecOfType") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiFile file = getContainingFile(element);
                if (file == null) {
                    return false;
                }
                String prefix = type.prefix(file);
                context.put(CONCORDION_SCHEMA_PREFIX, prefix);
                return type.usableWith(file) && prefix != null;
            }
        });
    }

    public Self withFullOgnl(boolean isUsingFullOgnl) {
        return with(new PatternCondition<T>("withFullOgnl") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiClass testFixture = context.get(TEST_FIXTURE);
                return testFixture != null && fullOgnlEnabled(testFixture) == isUsingFullOgnl;
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

    public Self withConfiguredExtensions(boolean canGuessPrefix) {
        return with(new PatternCondition<T>("withConfiguredExtensions") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiFile spec = getContainingFile(element);
                String extensionPrefix = spec != null ? extensionPrefixInFile(spec) : null;
                context.put(CONCORDION_EXTENSIONS_SCHEMA_PREFIX, extensionPrefix);

                Collection<String> extensions = configuredExtensions(context.get(TEST_FIXTURE));
                context.put(CONCORDION_EXTENSIONS, extensions);

                System.out.println("------------------");
                System.out.println(spec.getName());
                System.out.println(canGuessPrefix);
                System.out.println(extensionPrefix);
                System.out.println(extensions.size());

                return (canGuessPrefix || extensionPrefix != null) && !extensions.isEmpty();
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

    public Self withStartOfInjection() {
        return with(new PatternCondition<PsiElement>("withStartOfInjection") {
            @Override
            public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                return element.getTextOffset() == 0;
            }
        });
    }

    public Self withResolved(boolean resolved) {
        return with(new PatternCondition<T>("withResolved") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                return ((ConcordionPsiElement) element).isResolvable() == resolved;
            }
        });
    }

    public Self withTextNotMatching(@NotNull MultiPattern pattern) {
        return with(new PatternCondition<T>("withTextMatches") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                return !pattern.matches(element.getText());
            }
        });
    }

    public Self withCommand(@NotNull Predicate<ConcordionCommand> predicate) {
        return with(new PatternCondition<T>("withCommand") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                ConcordionCommand command = commandOf(element);
                context.put(CONCORDION_COMMAND, command);
                return predicate.test(command);
            }
        });
    }

    public static class Capture<T extends PsiElement> extends ConcordionElementPattern<T, Capture<T>> {

        protected Capture(final Class<T> aClass) {
            super(aClass);
        }
    }
}
