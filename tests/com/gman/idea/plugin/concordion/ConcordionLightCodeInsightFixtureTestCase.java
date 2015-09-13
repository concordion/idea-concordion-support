package com.gman.idea.plugin.concordion;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

public abstract class ConcordionLightCodeInsightFixtureTestCase extends JavaCodeInsightFixtureTestCase {

    protected VirtualFile copyJavaRunnerToConcordionProject(String javaRunner) {
        return  myFixture.copyFileToProject(getTestDataPath() + '/' + javaRunner, "/src/com/test/" + javaRunner);
    }

    protected VirtualFile copyHtmlSpecToConcordionProject(String htmlSpec) {
        return  myFixture.copyFileToProject(getTestDataPath() + '/' + htmlSpec, "/resources/com/test/" + htmlSpec);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        VirtualFile[] sourceRoots = ModuleRootManager.getInstance(myModule).getSourceRoots();
        if (sourceRoots.length == 1) {
            VirtualFile root = ModuleRootManager.getInstance(myModule).getSourceRoots()[0];
            Ref<VirtualFile> src = new Ref<>();
            Ref<VirtualFile> resources = new Ref<>();

            ApplicationManager.getApplication().runWriteAction(() -> {
                try {
                    src.set(root.createChildDirectory(this, "src"));
                    resources.set(root.createChildDirectory(this, "resources"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            PsiTestUtil.removeSourceRoot(myModule, root);
            PsiTestUtil.addSourceRoot(myModule, src.get(), JavaSourceRootType.TEST_SOURCE);
            PsiTestUtil.addSourceRoot(myModule, resources.get(), JavaResourceRootType.TEST_RESOURCE);
        }
    }
}
