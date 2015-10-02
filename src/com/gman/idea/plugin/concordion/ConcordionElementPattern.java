package com.gman.idea.plugin.concordion;

import com.intellij.openapi.util.Key;
import com.intellij.patterns.InitialPatternCondition;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionInjectionUtils.*;

public class ConcordionElementPattern<T extends PsiElement> extends PsiElementPattern<T,ConcordionElementPattern<T>> {

    public static final Key<PsiFile> HTML_SPEC = new Key<>("CONCORDION_HTML_SPEC");
    public static final Key<PsiClass> TEST_FIXTURE = new Key<>("CONCORDION_TEST_FIXTURE");

    public ConcordionElementPattern(final Class<T> aClass) {
        super(aClass);
    }

    public ConcordionElementPattern(@NotNull final InitialPatternCondition<T> condition) {
        super(condition);
    }

    public ConcordionElementPattern<T> withConcordionHtmlSpec() {
        return with(new PatternCondition<T>("withConcordionHtmlSpec") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                return isConcordionHtmlSpec(element.getContainingFile());
            }
        });
    }

    public ConcordionElementPattern<T> withFoundTestFixture() {
        return with(new PatternCondition<T>("withFoundTestFixture") {
            @Override
            public boolean accepts(@NotNull T element, ProcessingContext context) {
                PsiFile htmlSpec = element.getContainingFile();
                if (htmlSpec == null) {
                    htmlSpec = getTopLevelFile(element);
                }
                PsiClass testFixture = correspondingJavaRunner(htmlSpec);

                context.put(HTML_SPEC, htmlSpec);
                context.put(TEST_FIXTURE, testFixture);

                return testFixture != null;
            }
        });
    }

    public ConcordionElementPattern<T> withFullOgnl(boolean isUsingFullOgnl) {
        return with(new PatternCondition<T>("withFullOgnl") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                PsiClass testFixture = context.get(TEST_FIXTURE);
                return testFixture != null && isUsingFullOgnl(testFixture) == isUsingFullOgnl;
            }
        });
    }

    public ConcordionElementPattern<T> withConcordionSchemaAttribute() {
        return with(new PatternCondition<T>("withConcordionShemaAttribute") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                XmlAttribute attribute = PsiTreeUtil.getParentOfType(t, XmlAttribute.class);
                return attribute != null && isConcordionNamespace(attribute.getNamespace());
            }
        });
    }

    public ConcordionElementPattern<T> withConcordionCommand(Set<String> commands) {
        return with(new PatternCondition<T>("withConcordionCommand") {
            @Override
            public boolean accepts(@NotNull T t, ProcessingContext context) {
                XmlAttribute attribute = PsiTreeUtil.getParentOfType(t, XmlAttribute.class);
                return attribute != null && commands.contains(attribute.getLocalName());
            }
        });
    }
}
