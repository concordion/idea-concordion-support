package org.concordion.plugin.idea.configuration;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.execution.junit.TestClassConfigurationProducer;
import com.intellij.execution.junit.TestObject;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.concordion.plugin.idea.ConcordionNavigationService;
import org.jetbrains.annotations.Nullable;

import static org.concordion.plugin.idea.ConcordionPsiUtils.*;

public class ConcordionConfigurationProducer extends TestClassConfigurationProducer {

    @Override
    protected boolean setupConfigurationFromContext(JUnitConfiguration configuration, ConfigurationContext context, Ref<PsiElement> sourceElement) {
        PsiClass testFixture = testFixtureFrom(context);
        if (testFixture == null) {
            return false;
        }
        sourceElement.set(testFixture);
        setupConfigurationModule(context, configuration);
        final Module originalModule = configuration.getConfigurationModule().getModule();
        configuration.beClassConfiguration(testFixture);
        configuration.restoreOriginalModule(originalModule);
        configuration.setForkMode(JUnitConfiguration.FORK_NONE);
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(JUnitConfiguration unitConfiguration, ConfigurationContext context) {
        PsiClass testFixture = testFixtureFrom(context);
        TestObject testObject = unitConfiguration.getTestObject();

        return testObject != null && testObject.isConfiguredByElement(unitConfiguration, testFixture, null, null, null);
    }

    @Nullable
    private PsiClass testFixtureFrom(ConfigurationContext context) {
        PsiElement location = context.getPsiLocation();
        if (location == null) {
            return null;
        }
        PsiFile file = location.getContainingFile();
        if (file == null || !isConcordionHtmlSpec(file)) {
            return null;
        }
        return ConcordionNavigationService.getInstance(context.getProject()).correspondingJavaRunner(file);
    }
}
