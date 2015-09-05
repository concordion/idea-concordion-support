// This is a generated file. Not intended for manual editing.
package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gman.idea.plugin.concordion.lang.ConcordionElementType;
import com.gman.idea.plugin.concordion.lang.ConcordionTokenType;

public interface ConcordionTypes {

  IElementType ARGUMENTS = new ConcordionElementType("ARGUMENTS");
  IElementType CONCORDION_EXPRESSION = new ConcordionElementType("CONCORDION_EXPRESSION");
  IElementType CONCORDION_ITERATE_EXPRESSION = new ConcordionElementType("CONCORDION_ITERATE_EXPRESSION");
  IElementType CONCORDION_SET_EXPRESSION = new ConcordionElementType("CONCORDION_SET_EXPRESSION");
  IElementType INDEX = new ConcordionElementType("INDEX");
  IElementType LITERAL = new ConcordionElementType("LITERAL");
  IElementType METHOD = new ConcordionElementType("METHOD");
  IElementType OGNL_EXPRESSION = new ConcordionElementType("OGNL_EXPRESSION");
  IElementType PROPERTY = new ConcordionElementType("PROPERTY");
  IElementType VARIABLE = new ConcordionElementType("VARIABLE");

  IElementType CHARACTER_LITERAL = new ConcordionTokenType("CHARACTER_LITERAL");
  IElementType COLON = new ConcordionTokenType(":");
  IElementType COMA = new ConcordionTokenType(",");
  IElementType DOT = new ConcordionTokenType(".");
  IElementType DOUBLE_LITERAL = new ConcordionTokenType("DOUBLE_LITERAL");
  IElementType EQ = new ConcordionTokenType("=");
  IElementType HASH = new ConcordionTokenType("#");
  IElementType IDENTIFIER = new ConcordionTokenType("IDENTIFIER");
  IElementType INTEGER_LITERAL = new ConcordionTokenType("INTEGER_LITERAL");
  IElementType LBRACKET = new ConcordionTokenType("[");
  IElementType LPARENTH = new ConcordionTokenType("(");
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
      else if (type == CONCORDION_EXPRESSION) {
        return new ConcordionConcordionExpressionImpl(node);
      }
      else if (type == CONCORDION_ITERATE_EXPRESSION) {
        return new ConcordionConcordionIterateExpressionImpl(node);
      }
      else if (type == CONCORDION_SET_EXPRESSION) {
        return new ConcordionConcordionSetExpressionImpl(node);
      }
      else if (type == INDEX) {
        return new ConcordionIndexImpl(node);
      }
      else if (type == LITERAL) {
        return new ConcordionLiteralImpl(node);
      }
      else if (type == METHOD) {
        return new ConcordionMethodImpl(node);
      }
      else if (type == OGNL_EXPRESSION) {
        return new ConcordionOgnlExpressionImpl(node);
      }
      else if (type == PROPERTY) {
        return new ConcordionPropertyImpl(node);
      }
      else if (type == VARIABLE) {
        return new ConcordionVariableImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
