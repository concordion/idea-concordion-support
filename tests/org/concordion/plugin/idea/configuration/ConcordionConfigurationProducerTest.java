package org.concordion.plugin.idea.configuration;

import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionConfigurationProducerTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/configuration";
    }

    public void testCreateConfigurationForHtml() {
        copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConfigurationFromContext configuration = createRunConfiguration();
        assertThat(configuration).isNotNull();
        assertThat(((JUnitConfiguration) configuration.getConfiguration()).getRunClass()).isEqualTo("com.test.Spec");
        assertThat(canReuseRunConfigurationWhileReRunningFromSameContext(configuration)).isTrue();
    }

    public void testCreateConfigurationForXhtml() {
        copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec.xhtml");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(createRunConfiguration()).isNotNull();
    }

    public void testDoesNotCreateConfigurationForUnknownFormats() {
        copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec.txt");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(createRunConfiguration()).isNull();
    }
}