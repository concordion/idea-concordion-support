package com.gman.idea.plugin.concordion.lang;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class ConcordionParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(TokenType.DUMMY_HOLDER);

    public static final IFileElementType FILE = new IFileElementType(Language.findInstance(ConcordionLanguage.class));

    @NotNull
    public Lexer createLexer(Project project) {
        return new LexerAdapter();
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new ConcordionParser();
    }

    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return ConcordionTypes.Factory.createElement(node);
    }


    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ConcordionFile(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}