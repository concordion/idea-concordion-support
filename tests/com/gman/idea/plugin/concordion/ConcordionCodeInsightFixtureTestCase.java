package com.gman.idea.plugin.concordion;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

public abstract class ConcordionCodeInsightFixtureTestCase extends JavaCodeInsightFixtureTestCase {

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
            VirtualFile root = sourceRoots[0];
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

            PsiTestUtil.addLibrary(myModule, "testData/lib/hamcrest-core-1.3.jar");
            PsiTestUtil.addLibrary(myModule, "testData/lib/junit-4.12.jar");
            PsiTestUtil.addLibrary(myModule, "testData/lib/xom-1.2.5.jar");
            PsiTestUtil.addLibrary(myModule, "testData/lib/ognl-2.6.9.jar");
            PsiTestUtil.addLibrary(myModule, "testData/lib/concordion-1.5.1.jar");
        }
    }
}
