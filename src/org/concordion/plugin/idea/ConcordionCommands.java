package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.concordion.plugin.idea.settings.ConcordionCommandsCaseType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class ConcordionCommands {

    public static final Collection<String> CAMEL_CASE_COMMANDS = ImmutableList.of(
            "assertEquals", "assertTrue", "assertFalse", "verifyRows", "matchStrategy", "matchingRole"
    );

    public static final Collection<String> SPINAL_CASE_COMMANDS = ImmutableList.of(
            "assert-equals", "assert-true", "assert-false", "verify-rows", "match-strategy", "matching-role"
    );

    public static final Collection<String> SINGLE_WORD_COMMANDS = ImmutableList.of(
            "execute", "set", "echo", "run", "example", "status"
    );

    public static final Collection<String> ALL_COMMANDS = ImmutableList.<String>builder()
            .addAll(CAMEL_CASE_COMMANDS).addAll(SPINAL_CASE_COMMANDS).addAll(SINGLE_WORD_COMMANDS).build();

    public static final String EQUALS_EMBEDDED_FORM = "?";

    public static final String DEFAULT_PREFIX = "c";

    public static final Map<String, String> EXTENSION_COMMANDS = ImmutableMap.of(
            "org.concordion.ext.EmbedExtension", "embed",
            "org.concordion.ext.ExecuteOnlyIfExtension", "executeOnlyIf",
            "org.concordion.ext.ScreenshotExtension", "screenshot"
    );

    @NotNull
    public static Collection<String> commandsWithPrefix(@NotNull Collection<String> commands, @NotNull String prefix) {
        return commands.stream().map(command -> prefix + ':' + command).collect(toList());
    }

    @NotNull
    public static Collection<String> commands(@NotNull ConcordionCommandsCaseType type) {
        Collection<String> commands = new ArrayList<>();
        commands.addAll(SINGLE_WORD_COMMANDS);
        if (type.camelCase) {
            commands.addAll(CAMEL_CASE_COMMANDS);
        }
        if (type.spinalCase) {
            commands.addAll(SPINAL_CASE_COMMANDS);
        }
        return commands;
    }

    @NotNull
    public static Collection<String> embeddedCommands(@NotNull ConcordionCommandsCaseType type) {
        Collection<String> commands = commandsWithPrefix(commands(type), DEFAULT_PREFIX);
        commands.add(EQUALS_EMBEDDED_FORM);
        return commands;
    }

    @NotNull
    public static Collection<String> extensionCommands(@NotNull Collection<String> extensions) {
        return extensions.stream().map(EXTENSION_COMMANDS::get).filter(Objects::nonNull).collect(toList());
    }
}
