package org.concordion.plugin.idea;

import com.intellij.openapi.util.Key;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;

import java.util.Set;

public class ConcordionElementPattern<T extends PsiElement, Self extends ConcordionElementPattern<T, Self>> extends PsiElementPattern<T, Self> {

    public static final Key<PsiFile> HTML_SPEC = new Key<>("CONCORDION_HTML_SPEC");
    public static final Key<PsiClass> TEST_FIXTURE = new Key<>("CONCORDION_TEST_FIXTURE");
    public static final int PARENT_OF_THE_PARENT = 2;

    public ConcordionElementPattern(final Class<T> aClass) {
        super(aClass);
    }

    public Self withConcordionHtmlSpec() {
        return with(new PatternCondition<T>("withConcordionHtmlSpec") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                return ConcordionPsiUtils.isConcordionHtmlSpec(element.getContainingFile());
            }
        });
    }

    public Self withFoundTestFixture() {
        return with(new PatternCondition<T>("withFoundTestFixture") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiFile htmlSpec = element.getContainingFile();
                if (htmlSpec == null || htmlSpec.getParent() == null) {
                    htmlSpec = ConcordionInjectionUtils.getTopLevelFile(element);
                }
                if (htmlSpec == null) {
                    return false;
                }
                PsiClass testFixture = ConcordionNavigationService.getInstance(htmlSpec.getProject()).correspondingJavaRunner(htmlSpec);

                context.put(HTML_SPEC, htmlSpec);
                context.put(TEST_FIXTURE, testFixture);

                return testFixture != null;
            }
        });
    }

    public Self withFoundHtmlSpec() {
        return with(new PatternCondition<T>("withFoundHtmlSpec") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                PsiClass testFixture = PsiTreeUtil.getParentOfType(t, PsiClass.class);
                if (testFixture == null) {
                    return false;
                }
                PsiFile htmlSpec = ConcordionNavigationService.getInstance(testFixture.getProject()).correspondingHtmlSpec(testFixture);

                context.put(HTML_SPEC, htmlSpec);
                context.put(TEST_FIXTURE, testFixture);

                return htmlSpec != null;
            }
        });
    }

    public Self withFullOgnl(boolean isUsingFullOgnl) {
        return with(new PatternCondition<T>("withFullOgnl") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                PsiClass testFixture = context.get(TEST_FIXTURE);
                return testFixture != null && isUsingFullOgnl(testFixture) == isUsingFullOgnl;
            }

            private boolean isUsingFullOgnl(@NotNull PsiClass runnerClass) {
                return ConcordionPsiUtils.findAnnotationInClassHierarchy(runnerClass, ConcordionPsiTypeUtils.CONCORDION_FULL_OGNL) != null;
            }
        });
    }

    public Self withTestFixtureConfigured(boolean configured) {
        return with(new PatternCondition<T>("withJunitRunWithAnnotation") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                PsiClass testFixture = context.get(TEST_FIXTURE);
                return testFixture != null && concordionRunnerConfigured(testFixture) == configured;
            }

            private boolean concordionRunnerConfigured(@NotNull PsiClass testFixture) {
                PsiAnnotation runWithAnnotation = ConcordionPsiUtils.findAnnotationInClassHierarchy(testFixture, RunWith.class.getName());
                if (runWithAnnotation == null) {
                    return false;
                }
                String runnerQualifiedName = ConcordionPsiUtils.runnerQualifiedNameFromRunWithAnnotation(runWithAnnotation);
                return ConcordionPsiTypeUtils.CONCORDION_RUNNER.equals(runnerQualifiedName);
            }
        });
    }

    public Self withConcordionSchemaAttribute() {
        return with(new PatternCondition<T>("withConcordionShemaAttribute") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                XmlAttribute attribute = PsiTreeUtil.getParentOfType(t, XmlAttribute.class);
                return attribute != null && ConcordionPsiUtils.isConcordionNamespace(attribute.getNamespace());
            }
        });
    }

    public Self withConcordionCommand(Set<String> commands) {
        return with(new PatternCondition<T>("withConcordionCommand") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                XmlAttribute attribute = PsiTreeUtil.getParentOfType(t, XmlAttribute.class);
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
