// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.concordion.plugin.idea.lang.psi.ConcordionTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ConcordionParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == ARGUMENTS) {
      r = arguments(b, 0);
    }
    else if (t == FIELD) {
      r = field(b, 0);
    }
    else if (t == INDEX) {
      r = index(b, 0);
    }
    else if (t == ITERATE_EXPRESSION) {
      r = iterateExpression(b, 0);
    }
    else if (t == LITERAL) {
      r = literal(b, 0);
    }
    else if (t == METHOD) {
      r = method(b, 0);
    }
    else if (t == OGNL_EXPRESSION_NEXT) {
      r = ognlExpressionNext(b, 0);
    }
    else if (t == OGNL_EXPRESSION_START) {
      r = ognlExpressionStart(b, 0);
    }
    else if (t == SET_EXPRESSION) {
      r = setExpression(b, 0);
    }
    else if (t == VARIABLE) {
      r = variable(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  /* ********************************************************** */
  // ognlExpressionStart? (',' ognlExpressionStart)*
  public static boolean arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<arguments>");
    r = arguments_0(b, l + 1);
    r = r && arguments_1(b, l + 1);
    exit_section_(b, l, m, ARGUMENTS, r, false, null);
    return r;
  }

  // ognlExpressionStart?
  private static boolean arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_0")) return false;
    ognlExpressionStart(b, l + 1);
    return true;
  }

  // (',' ognlExpressionStart)*
  private static boolean arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!arguments_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arguments_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' ognlExpressionStart
  private static boolean arguments_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMA);
    r = r && ognlExpressionStart(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean field(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, FIELD, r);
    return r;
  }

  /* ********************************************************** */
  // setExpression|iterateExpression|ognlExpressionStart
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = setExpression(b, l + 1);
    if (!r) r = iterateExpression(b, l + 1);
    if (!r) r = ognlExpressionStart(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '[' ognlExpressionStart ']'
  public static boolean index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && ognlExpressionStart(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, INDEX, r);
    return r;
  }

  /* ********************************************************** */
  // variable ':' ognlExpressionStart
  public static boolean iterateExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "iterateExpression")) return false;
    if (!nextTokenIs(b, HASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && ognlExpressionStart(b, l + 1);
    exit_section_(b, m, ITERATE_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // STRING_LITERAL|DOUBLE_LITERAL|INTEGER_LITERAL
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<literal>");
    r = consumeToken(b, STRING_LITERAL);
    if (!r) r = consumeToken(b, DOUBLE_LITERAL);
    if (!r) r = consumeToken(b, INTEGER_LITERAL);
    exit_section_(b, l, m, LITERAL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER '(' arguments ')'
  public static boolean method(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && consumeToken(b, LPARENTH);
    r = r && arguments(b, l + 1);
    r = r && consumeToken(b, RPARENTH);
    exit_section_(b, m, METHOD, r);
    return r;
  }

  /* ********************************************************** */
  // (method|field) index* ('.' ognlExpressionNext)*
  public static boolean ognlExpressionNext(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionNext")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, null);
    r = ognlExpressionNext_0(b, l + 1);
    r = r && ognlExpressionNext_1(b, l + 1);
    r = r && ognlExpressionNext_2(b, l + 1);
    exit_section_(b, l, m, OGNL_EXPRESSION_NEXT, r, false, null);
    return r;
  }

  // method|field
  private static boolean ognlExpressionNext_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionNext_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = method(b, l + 1);
    if (!r) r = field(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // index*
  private static boolean ognlExpressionNext_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionNext_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!index(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ognlExpressionNext_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ('.' ognlExpressionNext)*
  private static boolean ognlExpressionNext_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionNext_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!ognlExpressionNext_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ognlExpressionNext_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // '.' ognlExpressionNext
  private static boolean ognlExpressionNext_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionNext_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ognlExpressionNext(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (literal|method|field|variable) index* ('.' ognlExpressionNext)?
  public static boolean ognlExpressionStart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionStart")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<ognl expression start>");
    r = ognlExpressionStart_0(b, l + 1);
    r = r && ognlExpressionStart_1(b, l + 1);
    r = r && ognlExpressionStart_2(b, l + 1);
    exit_section_(b, l, m, OGNL_EXPRESSION_START, r, false, null);
    return r;
  }

  // literal|method|field|variable
  private static boolean ognlExpressionStart_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionStart_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = literal(b, l + 1);
    if (!r) r = method(b, l + 1);
    if (!r) r = field(b, l + 1);
    if (!r) r = variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // index*
  private static boolean ognlExpressionStart_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionStart_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!index(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ognlExpressionStart_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ('.' ognlExpressionNext)?
  private static boolean ognlExpressionStart_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionStart_2")) return false;
    ognlExpressionStart_2_0(b, l + 1);
    return true;
  }

  // '.' ognlExpressionNext
  private static boolean ognlExpressionStart_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ognlExpressionStart_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ognlExpressionNext(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable '=' ognlExpressionStart
  public static boolean setExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "setExpression")) return false;
    if (!nextTokenIs(b, HASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && ognlExpressionStart(b, l + 1);
    exit_section_(b, m, SET_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // '#' IDENTIFIER
  public static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    if (!nextTokenIs(b, HASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HASH);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, m, VARIABLE, r);
    return r;
  }

}
