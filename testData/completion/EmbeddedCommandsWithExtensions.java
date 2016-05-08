package com.test;

import org.concordion.api.extension.Extensions;
import org.concordion.api.option.ConcordionOptions;
import org.concordion.ext.EmbedExtension;
import org.concordion.ext.ExecuteOnlyIfExtension;
import org.concordion.ext.ScreenshotExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions({
        EmbedExtension.class,
        ExecuteOnlyIfExtension.class,
        ScreenshotExtension.class
})
@ConcordionOptions(
        declareNamespaces = {"ext", "urn:concordion-extensions:2010"}
)
public class EmbeddedCommandsWithExtensions {

}
