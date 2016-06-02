package com.test;

import org.concordion.api.extension.Extensions;
import org.concordion.api.option.ConcordionOptions;
import org.concordion.ext.EmbedExtension;
import org.concordion.ext.ExecuteOnlyIfExtension;
import org.concordion.ext.ScreenshotExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(classOf[ConcordionRunner])
@Extensions(Array(
        classOf[EmbedExtension],
        classOf[ExecuteOnlyIfExtension],
        classOf[ScreenshotExtension]
))
@ConcordionOptions(
        declareNamespaces = Array("ext", "urn:concordion-extensions:2010")
)
class EmbeddedCommandsWithExtensions {

}
