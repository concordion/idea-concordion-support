package org.concordion.plugin.idea.lang;

import com.intellij.lexer.FlexAdapter;

public class ConcordionLexerFactory {

	public static FlexAdapter createConcordionLexer() {
		return new FlexAdapter(new ConcordionLexer());
	}
}
