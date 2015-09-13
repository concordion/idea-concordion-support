package com.gman.idea.plugin.concordion;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

public abstract class ConcordionLightCodeInsightFixtureTestCase extends LightCodeInsightFixtureTestCase {

    private static final String SOURCES_DIR = "/src/src";
    private static final String RESOURCES_DIR = "/src/resources";
    private static final String PROJECT_PACKAGE = "/com/test/";

    protected VirtualFile copyJavaRunnerToConcordionProject(String javaRunner) {
        return  myFixture.copyFileToProject(getTestDataPath() + '/' + javaRunner, SOURCES_DIR + PROJECT_PACKAGE + javaRunner);
    }

    protected VirtualFile copyHtmlSpecToConcordionProject(String htmlSpec) {
        return  myFixture.copyFileToProject(getTestDataPath() + '/' + htmlSpec, SOURCES_DIR + PROJECT_PACKAGE + htmlSpec);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ModifiableRootModel model = ModuleRootManager.getInstance(myFixture.getModule()).getModifiableModel();
        ContentEntry contentEntry = model.getContentEntries()[0];
        contentEntry.removeSourceFolder(contentEntry.getSourceFolders()[0]);
        model.getContentEntries()[0].addSourceFolder("temp://" + SOURCES_DIR, JavaSourceRootType.TEST_SOURCE);
        model.getContentEntries()[0].addSourceFolder("temp://" + RESOURCES_DIR, JavaResourceRootType.TEST_RESOURCE);
        ApplicationManager.getApplication().runWriteAction(() -> {
            model.commit();
            myFixture.getModule().getProject().save();
        });
    }
}
