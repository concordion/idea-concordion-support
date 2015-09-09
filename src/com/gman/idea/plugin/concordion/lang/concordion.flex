package com.gman.idea.plugin.concordion.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes;
import com.intellij.psi.TokenType;

%%
%class ConcordionLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%{
  public ConcordionLexer(){
    this((java.io.Reader)null);
  }

  int braceCount;
%}

ALPHA=[:letter:]
DIGIT=[0-9]
WHITE_SPACE_CHAR=[\ \n\r\t\f]

IDENTIFIER={ALPHA} [:jletterdigit:]*

INTEGER_LITERAL=(0|([1-9]({DIGIT})*))

DOUBLE_LITERAL=({DOUBLE_LITERAL_1})|({DOUBLE_LITERAL_2})|({DOUBLE_LITERAL_3})
DOUBLE_LITERAL_1=({DIGIT})+"."({DIGIT})*({EXPONENT_PART})?
DOUBLE_LITERAL_2="."({DIGIT})+({EXPONENT_PART})?
DOUBLE_LITERAL_3=({DIGIT})+({EXPONENT_PART})
EXPONENT_PART=[Ee]["+""-"]?({DIGIT})*

STRING_LITERAL="'"([^\\\'\r\n]|{ESCAPE_SEQUENCE})*("'"|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

%%

{WHITE_SPACE_CHAR}+   { return TokenType.WHITE_SPACE; }

{INTEGER_LITERAL}     { return ConcordionTypes.INTEGER_LITERAL; }
{DOUBLE_LITERAL}      { return ConcordionTypes.DOUBLE_LITERAL; }
{STRING_LITERAL}      { return ConcordionTypes.STRING_LITERAL; }

{IDENTIFIER} { return ConcordionTypes.IDENTIFIER; }

"("  { return ConcordionTypes.LPARENTH; }
")"  { return ConcordionTypes.RPARENTH; }
"["  { return ConcordionTypes.LBRACKET; }
"]"  { return ConcordionTypes.RBRACKET; }

"."  { return ConcordionTypes.DOT; }
","  { return ConcordionTypes.COMA; }
"#"  { return ConcordionTypes.HASH; }

"="  { return ConcordionTypes.EQ; }
":"  { return ConcordionTypes.COLON; }
";"  { return ConcordionTypes.SEMICOLON; }

.    {  yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }