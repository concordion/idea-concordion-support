package org.concordion.plugin.idea;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ConcordionCommands {

    public static final Collection<String> DEFAULT_COMMANDS = ImmutableList.of(
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "execute",
            "set",
            "echo",
            "verifyRows", "verify-rows",
            "matchStrategy", "match-strategy",
            "matchingRole", "matching-role",
            "run",
            "example",
            "status"
    );

    public static final Collection<String> DEFAULT_COMMANDS_WITH_C_PREFIX = commandsWithPrefix(DEFAULT_COMMANDS, "c");

    public static final Collection<String> EMBEDDED_COMMANDS = ImmutableList.<String>builder().addAll(DEFAULT_COMMANDS_WITH_C_PREFIX).add("?").build();

    private static final Map<String, String> EXTENSION_COMMANDS = ImmutableMap.of(
            "org.concordion.ext.EmbedExtension", "embed",
            "org.concordion.ext.ExecuteOnlyIfExtension", "executeOnlyIf",
            "org.concordion.ext.ScreenshotExtension", "screenshot");

    @NotNull
    public static Collection<String> commandsWithPrefix(@NotNull Collection<String> commands, @NotNull String prefix) {
        return commands.stream().map(command -> prefix + ':' + command).collect(toList());
    }

    @NotNull
    public static Collection<String> extensionCommands(@NotNull Collection<String> extensions) {
        return extensions.stream().map(EXTENSION_COMMANDS::get).filter(Objects::nonNull).collect(toList());
    }

    @NotNull
    public static String removePrefixIfPresent(@NotNull String command) {
        int separator = command.indexOf(':');
        return separator != -1 ? command.substring(separator + 1) : command;
    }

    @Nullable
    public static String findCommandInMdInjection(@NotNull String text) {
        return EMBEDDED_COMMANDS.stream()
                .filter(command -> text.startsWith(command, 1))
                .findFirst().orElse(null);
    }
}
