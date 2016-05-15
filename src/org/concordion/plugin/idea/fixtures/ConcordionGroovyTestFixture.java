package org.concordion.plugin.idea.fixtures;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ConcordionGroovyTestFixture implements ConcordionTestFixture {

    @NotNull
    @Override
    public Set<String> fileExtensions() {
        return ImmutableSet.of("groovy");
    }
}
