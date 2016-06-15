package org.concordion.plugin.idea.menu;

import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.openapi.actionSystem.*;
import org.concordion.plugin.idea.ConcordionExtension;
import org.concordion.plugin.idea.lang.ConcordionIcons;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static org.concordion.plugin.idea.fixtures.ConcordionTestFixtures.fixtures;
import static org.concordion.plugin.idea.specifications.ConcordionSpecifications.specifications;

public class ConcordionSpecMenu extends DefaultActionGroup {

    public ConcordionSpecMenu() {
        setPopup(true);
        Presentation presentation = getTemplatePresentation();
        presentation.setText("Concordion");
        presentation.setIcon(ConcordionIcons.ICON);

        add(specAndFixtureDialog());
        add(createTemplatesMenuFor(fixtures(), "Fixture"));
        add(createTemplatesMenuFor(specifications(), "Specification"));
    }

    @NotNull
    private static AnAction specAndFixtureDialog() {
        return new AnAction("Spec and fixture") {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                ConcordionNewSpecAndFixtureDialog.show(anActionEvent);
            }
        };
    }

    @NotNull
    private static DefaultActionGroup createTemplatesMenuFor(
            @NotNull Stream<? extends ConcordionExtension> extensions,
            @NotNull String name
    ) {
        DefaultActionGroup templates = new DefaultActionGroup();
        templates.setPopup(true);
        templates.getTemplatePresentation().setText(name);

        extensions
                .map(ConcordionExtension::fileTemplate)
                .map(CreateFromTemplateAction::new)
                .forEach(templates::add);

        return templates;
    }
}
