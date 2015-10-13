package org.concordion.plugin.idea.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConcordionFileType extends LanguageFileType {

    public static final ConcordionFileType INSTANCE = new ConcordionFileType();

    private ConcordionFileType() {
        super(ConcordionLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "concordion";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "concordion expression language";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "concordion";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return ConcordionIcons.ICON;
    }
}
