package com.test;

import org.junit.runner.RunWith;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.api.extension.Extensions;

import org.concordion.ext.EmbedExtension;
import org.concordion.ext.ExecuteOnlyIfExtension;
import org.concordion.ext.ScreenshotExtension;

@RunWith(classOf[ConcordionRunner])
@Extensions({
        classOf[EmbedExtension],
        classOf[ExecuteOnlyIfExtension],
        classOf[ScreenshotExtension]
})
class ExtensionsCommands {

}
