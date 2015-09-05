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
DOUBLE_LITERAL=({FLOATING_POINT_LITERAL1})|({FLOATING_POINT_LITERAL2})|({FLOATING_POINT_LITERAL3})

FLOATING_POINT_LITERAL1=({DIGIT})+"."({DIGIT})*({EXPONENT_PART})?
FLOATING_POINT_LITERAL2="."({DIGIT})+({EXPONENT_PART})?
FLOATING_POINT_LITERAL3=({DIGIT})+({EXPONENT_PART})
EXPONENT_PART=[Ee]["+""-"]?({DIGIT})*

CHARACTER_LITERAL="'"([^\\\'\r\n]|{ESCAPE_SEQUENCE})*("'"|\\)?
STRING_LITERAL=\"([^\\\"\r\n]|{ESCAPE_SEQUENCE})*(\"|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

%%

{WHITE_SPACE_CHAR}+   { return TokenType.WHITE_SPACE; }

{INTEGER_LITERAL}     { return ConcordionTypes.INTEGER_LITERAL; }
{DOUBLE_LITERAL}      { return ConcordionTypes.DOUBLE_LITERAL; }
{CHARACTER_LITERAL}   { return ConcordionTypes.CHARACTER_LITERAL; }
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

//"!=" { return ConcordionTypes.NOT_EQUAL; }
//"!"  { return ConcordionTypes.NEGATE; }
//"==" { return ConcordionTypes.EQUAL; }
//
//"<=" { return ConcordionTypes.LESS_EQUAL; }
//">=" { return ConcordionTypes.GREATER_EQUAL; }
//"<"  { return ConcordionTypes.LESS; }
//">"  { return ConcordionTypes.GREATER; }
//
//"/"  { return ConcordionTypes.DIVISION; }
//"*"  { return ConcordionTypes.MULTIPLY; }
//"-"  { return ConcordionTypes.MINUS; }
//"+"  { return ConcordionTypes.PLUS; }
//"%"  { return ConcordionTypes.MODULO; }
//
//"&&" { return ConcordionTypes.AND_AND; }
//"||" { return ConcordionTypes.OR_OR; }

.    {  yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }