// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import org.concordion.plugin.idea.lang.ConcordionElementType;
import org.concordion.plugin.idea.lang.ConcordionTokenType;

public interface ConcordionTypes {

  IElementType ARGUMENTS = new ConcordionElementType("ARGUMENTS");
  IElementType EMBEDDED_COMMAND = new ConcordionElementType("EMBEDDED_COMMAND");
  IElementType FIELD = new ConcordionElementType("FIELD");
  IElementType INDEX = new ConcordionElementType("INDEX");
  IElementType ITERATE_EXPRESSION = new ConcordionElementType("ITERATE_EXPRESSION");
  IElementType LIST = new ConcordionElementType("LIST");
  IElementType LITERAL = new ConcordionElementType("LITERAL");
  IElementType MAP = new ConcordionElementType("MAP");
  IElementType MAP_ENTRY = new ConcordionElementType("MAP_ENTRY");
  IElementType METHOD = new ConcordionElementType("METHOD");
  IElementType OGNL_EXPRESSION_NEXT = new ConcordionElementType("OGNL_EXPRESSION_NEXT");
  IElementType OGNL_EXPRESSION_START = new ConcordionElementType("OGNL_EXPRESSION_START");
  IElementType SET_EXPRESSION = new ConcordionElementType("SET_EXPRESSION");
  IElementType VARIABLE = new ConcordionElementType("VARIABLE");

  IElementType COLON = new ConcordionTokenType(":");
  IElementType COMA = new ConcordionTokenType(",");
  IElementType COMMAND = new ConcordionTokenType("COMMAND");
  IElementType DOT = new ConcordionTokenType(".");
  IElementType DOUBLE_LITERAL = new ConcordionTokenType("DOUBLE_LITERAL");
  IElementType EQ = new ConcordionTokenType("=");
  IElementType HASH = new ConcordionTokenType("#");
  IElementType IDENTIFIER = new ConcordionTokenType("IDENTIFIER");
  IElementType INTEGER_LITERAL = new ConcordionTokenType("INTEGER_LITERAL");
  IElementType LBRACE = new ConcordionTokenType("{");
  IElementType LBRACKET = new ConcordionTokenType("[");
  IElementType LPARENTH = new ConcordionTokenType("(");
  IElementType RBRACE = new ConcordionTokenType("}");
  IElementType RBRACKET = new ConcordionTokenType("]");
  IElementType RPARENTH = new ConcordionTokenType(")");
  IElementType SEMICOLON = new ConcordionTokenType(";");
  IElementType STRING_LITERAL = new ConcordionTokenType("STRING_LITERAL");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ARGUMENTS) {
        return new ConcordionArgumentsImpl(node);
      }
      else if (type == EMBEDDED_COMMAND) {
        return new ConcordionEmbeddedCommandImpl(node);
      }
      else if (type == FIELD) {
        return new ConcordionFieldImpl(node);
      }
      else if (type == INDEX) {
        return new ConcordionIndexImpl(node);
      }
      else if (type == ITERATE_EXPRESSION) {
        return new ConcordionIterateExpressionImpl(node);
      }
      else if (type == LIST) {
        return new ConcordionListImpl(node);
      }
      else if (type == LITERAL) {
        return new ConcordionLiteralImpl(node);
      }
      else if (type == MAP) {
        return new ConcordionMapImpl(node);
      }
      else if (type == MAP_ENTRY) {
        return new ConcordionMapEntryImpl(node);
      }
      else if (type == METHOD) {
        return new ConcordionMethodImpl(node);
      }
      else if (type == OGNL_EXPRESSION_NEXT) {
        return new ConcordionOgnlExpressionNextImpl(node);
      }
      else if (type == OGNL_EXPRESSION_START) {
        return new ConcordionOgnlExpressionStartImpl(node);
      }
      else if (type == SET_EXPRESSION) {
        return new ConcordionSetExpressionImpl(node);
      }
      else if (type == VARIABLE) {
        return new ConcordionVariableImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
