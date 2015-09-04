// This is a generated file. Not intended for manual editing.
package com.gman.idea.plugin.concordion.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gman.idea.plugin.concordion.lang.ConcordionElementType;
import com.gman.idea.plugin.concordion.lang.ConcordionTokenType;

public interface ConcordionTypes {

  IElementType EXPR = new ConcordionElementType("EXPR");
  IElementType LITERAL = new ConcordionElementType("LITERAL");
  IElementType METHOD = new ConcordionElementType("METHOD");
  IElementType PROPERTY = new ConcordionElementType("PROPERTY");

  IElementType CHARACTER_LITERAL = new ConcordionTokenType("CHARACTER_LITERAL");
  IElementType DOUBLE_LITERAL = new ConcordionTokenType("DOUBLE_LITERAL");
  IElementType IDENTIFIER = new ConcordionTokenType("IDENTIFIER");
  IElementType INTEGER_LITERAL = new ConcordionTokenType("INTEGER_LITERAL");
  IElementType LPARENTH = new ConcordionTokenType("(");
  IElementType RPARENTH = new ConcordionTokenType(")");
  IElementType STRING_LITERAL = new ConcordionTokenType("STRING_LITERAL");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == EXPR) {
        return new ConcordionExprImpl(node);
      }
      else if (type == LITERAL) {
        return new ConcordionLiteralImpl(node);
      }
      else if (type == METHOD) {
        return new ConcordionMethodImpl(node);
      }
      else if (type == PROPERTY) {
        return new ConcordionPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
