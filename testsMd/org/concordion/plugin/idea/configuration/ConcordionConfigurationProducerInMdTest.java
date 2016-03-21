package org.concordion.plugin.idea.configuration;

import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcordionConfigurationProducerInMdTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/configuration";
    }

    public void testCreateConfigurationForMd() {
        copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec.md");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConfigurationFromContext configuration = createRunConfiguration();
        assertThat(configuration).isNotNull();
        assertThat(((JUnitConfiguration) configuration.getConfiguration()).getRunClass()).isEqualTo("com.test.Spec");
        assertThat(canReuseRunConfigurationWhileReRunningFromSameContext(configuration)).isTrue();
    }

    public void testCreateConfigurationForMarkdown() {
        copyTestFixtureToConcordionProject("Spec.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("Spec.markdown");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        assertThat(createRunConfiguration()).isNotNull();
    }
}