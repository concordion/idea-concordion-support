// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class ConcordionVisitor extends PsiElementVisitor {

  public void visitArguments(@NotNull ConcordionArguments o) {
    visitPsiElement(o);
  }

  public void visitEmbeddedCommand(@NotNull ConcordionEmbeddedCommand o) {
    visitPsiElement(o);
  }

  public void visitField(@NotNull ConcordionField o) {
    visitFieldInternal(o);
  }

  public void visitIndex(@NotNull ConcordionIndex o) {
    visitPsiElement(o);
  }

  public void visitIterateExpression(@NotNull ConcordionIterateExpression o) {
    visitPsiElement(o);
  }

  public void visitList(@NotNull ConcordionList o) {
    visitPsiElement(o);
  }

  public void visitLiteral(@NotNull ConcordionLiteral o) {
    visitPsiElement(o);
  }

  public void visitMap(@NotNull ConcordionMap o) {
    visitPsiElement(o);
  }

  public void visitMapEntry(@NotNull ConcordionMapEntry o) {
    visitPsiElement(o);
  }

  public void visitMethod(@NotNull ConcordionMethod o) {
    visitMethodInternal(o);
  }

  public void visitOgnlExpressionNext(@NotNull ConcordionOgnlExpressionNext o) {
    visitPsiElement(o);
  }

  public void visitOgnlExpressionStart(@NotNull ConcordionOgnlExpressionStart o) {
    visitPsiElement(o);
  }

  public void visitSetExpression(@NotNull ConcordionSetExpression o) {
    visitPsiElement(o);
  }

  public void visitStatement(@NotNull ConcordionStatement o) {
    visitPsiElement(o);
  }

  public void visitVariable(@NotNull ConcordionVariable o) {
    visitVariableInternal(o);
  }

  public void visitFieldInternal(@NotNull ConcordionFieldInternal o) {
    visitPsiElement(o);
  }

  public void visitMethodInternal(@NotNull ConcordionMethodInternal o) {
    visitPsiElement(o);
  }

  public void visitVariableInternal(@NotNull ConcordionVariableInternal o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
