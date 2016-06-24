package org.concordion.plugin.idea;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.execution.Location;
import com.intellij.execution.PsiLocation;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.MapDataContext;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.VfsTestUtil;
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.configuration.ConcordionConfigurationProducer;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.concordion.plugin.idea.settings.ConcordionSettings;
import org.concordion.plugin.idea.settings.ConcordionSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ConcordionCodeInsightFixtureTestCase extends JavaCodeInsightFixtureTestCase {

    protected VirtualFile copyTestFixtureToConcordionProject(@NotNull String javaRunner, @NotNull String... templateValues) {
        return assertInTestPackage(copyTemplateFileToConcordionProject(getTestDataPath() + '/' + javaRunner, "/src/com/test/" + javaRunner, templateValues));
    }

    protected VirtualFile copySpecToConcordionProject(@NotNull String htmlSpec, @NotNull String... templateValues) {
        return copyTemplateFileToConcordionProject(getTestDataPath() + '/' + htmlSpec, "/resources/com/test/" + htmlSpec, templateValues);
    }

    protected VirtualFile copyTemplateFileToConcordionProject(
            @NotNull String sourcePath,
            @NotNull String targetPath,
            @NotNull String... templateValues
    ) {
        File sourceFile = new File(sourcePath);

        assertThat(sourceFile.exists()).isTrue();
        assertThat(sourceFile.isFile()).isTrue();
        assertThat(myFixture.getTempDirFixture().getFile(targetPath)).isNull();

        VirtualFile targetFile = myFixture.getTempDirFixture().createFile(targetPath);
        VfsTestUtil.assertFilePathEndsWithCaseSensitivePath(targetFile, sourcePath);
        targetFile.putUserData(VfsTestUtil.TEST_DATA_FILE_PATH, sourceFile.getAbsolutePath());

        writeAction(() -> templateValues.length == 0 ? copy(sourceFile, targetFile) : copyTemplate(sourceFile, targetFile, templateValues));

        return targetFile;
    }

    private static Void copy(@NotNull File sourceFile, @NotNull VirtualFile targetFile) throws IOException {
        targetFile.setBinaryContent(FileUtil.loadFileBytes(sourceFile));
        return null;
    }

    private static Void copyTemplate(@NotNull File sourceFile, @NotNull VirtualFile targetFile, @NotNull String[] templateValues) throws IOException {
        String source = FileUtil.loadFile(sourceFile);
        for (int i = 0; i < templateValues.length; i++) {
            source = source.replaceAll("\\$\\{" + i + "\\}", templateValues[i]);
        }
        VfsUtil.saveText(targetFile, source);
        return null;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        VirtualFile[] sourceRoots = ModuleRootManager.getInstance(myModule).getSourceRoots();
        if (sourceRoots.length == 1) {
            VirtualFile root = sourceRoots[0];
            Ref<VirtualFile> src = new Ref<>();
            Ref<VirtualFile> resources = new Ref<>();

            writeAction(() -> {
                src.set(root.createChildDirectory(this, "src"));
                resources.set(root.createChildDirectory(this, "resources"));
                return null;
            });

            PsiTestUtil.removeSourceRoot(myModule, root);
            PsiTestUtil.addSourceRoot(myModule, src.get(), JavaSourceRootType.TEST_SOURCE);
            PsiTestUtil.addSourceRoot(myModule, resources.get(), JavaResourceRootType.TEST_RESOURCE);

            //JDK
            PsiTestUtil.addLibrary(myModule, "testData/lib/rt.jar");
            //Concordion dependencies
            PsiTestUtil.addLibrary(myModule, "testData/lib/junit-4.12.jar");
            //Concordion itself
            PsiTestUtil.addLibrary(myModule, "testData/lib/concordion-2.0.0.jar");
            //Concordion extensions
            PsiTestUtil.addLibrary(myModule, "testData/lib/concordion-embed-extension-1.1.2.jar");
            PsiTestUtil.addLibrary(myModule, "testData/lib/concordion-executeonlyif-extension-0.2.1.jar");
            PsiTestUtil.addLibrary(myModule, "testData/lib/concordion-screenshot-extension-1.2.0.jar");
        }
    }

    protected final VirtualFile assertInTestPackage(@NotNull VirtualFile javaFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(javaFile.getInputStream()))) {
            assertThat(reader.readLine()).startsWith("package com.test");
            return javaFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    protected final PsiFile findFileInProject(@NotNull String path) {
        VirtualFile file = myFixture.getTempDirFixture().getFile(path);
        return file != null ? PsiManager.getInstance(myFixture.getProject()).findFile(file) : null;
    }

    protected final void assertHasGutters(VirtualFile fixture, VirtualFile spec) {
        myFixture.configureFromExistingVirtualFile(fixture);
        GuttersAssert.assertThat(myFixture.findAllGutters()).hasConcordionGutter();

        myFixture.configureFromExistingVirtualFile(spec);
        GuttersAssert.assertThat(myFixture.findAllGutters()).hasConcordionGutter();
    }

    protected final void assertHasNoGutters(VirtualFile fixture, VirtualFile spec) {
        myFixture.configureFromExistingVirtualFile(fixture);
        GuttersAssert.assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();

        myFixture.configureFromExistingVirtualFile(spec);
        GuttersAssert.assertThat(myFixture.findAllGutters()).hasNoConcordionGutter();
    }

    protected final void assertNavigateToEachOther(VirtualFile testFixture, VirtualFile spec) {
        ConcordionNavigationService navigation = ConcordionNavigationService.getInstance(getProject());

        PsiFile testFixtureFile = PsiManager.getInstance(getProject()).findFile(testFixture);
        PsiFile specFile = PsiManager.getInstance(getProject()).findFile(spec);

        assertThat(navigation.pairedFile(testFixtureFile)).isNotNull().isEqualTo(specFile);
        assertThat(navigation.pairedFile(specFile)).isNotNull().isEqualTo(testFixtureFile);

    }

    protected final void assertDoNotNavigate(VirtualFile... files) {
        ConcordionNavigationService navigation = ConcordionNavigationService.getInstance(getProject());

        for (VirtualFile file : files) {
            assertThat(navigation.pairedFile(PsiManager.getInstance(getProject()).findFile(file))).isNull();

        }
    }

    protected final <T extends PsiElement> T elementUnderCaret() {
        return (T) myFixture.getFile().findElementAt(myFixture.getCaretOffset() - 1).getParent();
    }

    protected final <T extends PsiElement> T resolveReferences(PsiElement e) {
        PsiReference[] references = e.getReferences();
        assertThat(references).hasSize(1);
        return (T) references[0].resolve();
    }

    @NotNull
    private ConfigurationContext createRunConfigurationContext() {
        MapDataContext dataContext = new MapDataContext();
        dataContext.put(CommonDataKeys.PROJECT, myFixture.getProject());
        dataContext.put(LangDataKeys.MODULE, myFixture.getModule());
        dataContext.put(Location.DATA_KEY, PsiLocation.fromPsiElement(myFixture.getFile()));

        return ConfigurationContext.getFromContext(dataContext);
    }

    protected final ConfigurationFromContext createRunConfiguration() {
        return RunConfigurationProducer.getInstance(ConcordionConfigurationProducer.class)
                .createConfigurationFromContext(createRunConfigurationContext());
    }

    protected final boolean canReuseRunConfigurationWhileReRunningFromSameContext(@NotNull ConfigurationFromContext configuration) {
        return RunConfigurationProducer.getInstance(ConcordionConfigurationProducer.class)
                .isConfigurationFromContext((JUnitConfiguration) configuration.getConfiguration(), createRunConfigurationContext());
    }

    protected final void writeAction(@NotNull ThrowableComputable<Void, Throwable> action) {
        new WriteCommandAction(myFixture.getProject()){
            @Override
            protected void run(@NotNull final Result result) throws Throwable {
                action.compute();
            }
        }.execute();
    }

    protected final void execute(@NotNull IntentionAction action) {
        assertTrue(action.isAvailable(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile()));

        writeAction(() -> {
            action.invoke(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile());
            return null;
        });
    }

    protected final void executeIntention(@NotNull String name) {
        execute(myFixture.findSingleIntention(name));
    }

    protected final void executeQuickFix(@NotNull HighlightingAssert.Info info) {
        execute(myFixture.doHighlighting().stream()
                .filter(info::match)
                .flatMap(highlight -> highlight.quickFixActionRanges.stream())
                .map(highlight -> highlight.getFirst().getAction())
                .findFirst().orElseThrow(() -> new AssertionError("Expects single fix")));
    }

    protected final void useCommandsCase(ConcordionCommandsCaseType type) {
        ConcordionSettingsState settings = new ConcordionSettingsState();
        settings.setCommandsCaseType(type);
        ConcordionSettings.getInstance().updateState(settings);
    }
}
