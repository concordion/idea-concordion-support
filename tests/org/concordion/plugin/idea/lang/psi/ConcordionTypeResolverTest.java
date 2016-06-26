package org.concordion.plugin.idea.lang.psi;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;
import org.concordion.plugin.idea.ConcordionPsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.concordion.plugin.idea.ConcordionPsiUtils.typeOfExpression;

public class ConcordionTypeResolverTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/type";
    }

    public void testTypeOfUnresolvedMethod() {
        assertTypeOfExpression("iAmNotReal()", null);
    }

    public void testTypeOfStringMethod() {
        assertTypeOfExpression("stringMethod()", "java.lang.String");
    }

    public void testTypeOfArrayLengthAfterMethod() {
        assertTypeOfExpression("arrayMethod().length", PsiType.INT.getCanonicalText());
    }

    public void testTypeOfArrayItemReturnedByMethod() {
        assertTypeOfExpression("arrayMethod()[0]", "java.lang.String");
    }

    public void testTypeOfListMethod() {
        assertTypeOfExpression("listMethod()", "java.util.List<java.lang.String>");
    }

    public void ignoredTestTypeOfListItemReturnedByGetFromMethod() {
        assertTypeOfExpression("listMethod().get(0)", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByBracketsFromMethod() {
        assertTypeOfExpression("listMethod()[0]", "java.lang.String");
    }

    public void testTypeOfMapMethod() {
        assertTypeOfExpression("mapMethod()", "java.util.Map<java.lang.Integer,java.lang.String>");
    }

    public void ignoredTestTypeOfMapValueReturnedByGetFromMethod() {
        assertTypeOfExpression("mapMethod().get(0)", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByBracketsFromMethod() {
        assertTypeOfExpression("mapMethod()[0]", "java.lang.String");
    }

    public void testTypeOfUserTypeMethod() {
        assertTypeOfExpression("userTypeMethod()", "com.test.ResolvingTypeOfExpression.Nested");
    }

    public void testTypeOfNestedFieldOfUserTypeMethod() {
        assertTypeOfExpression("userTypeMethod().nestedStringField", "java.lang.String");
    }

    public void testTypeOfNestedMethodOfUserTypeMethod() {
        assertTypeOfExpression("userTypeMethod().nestedStringMethod()", "java.lang.String");
    }



    public void testTypeOfUnresolvedField() {
        assertTypeOfExpression("iAmNotReal", null);
    }

    public void testTypeOfField() {
        assertTypeOfExpression("stringField", "java.lang.String");
    }

    public void testTypeOfArrayLengthAfterField() {
        assertTypeOfExpression("arrayField.length", PsiType.INT.getCanonicalText());
    }

    public void testTypeOfArrayItemReturnedByField() {
        assertTypeOfExpression("arrayField[0]", "java.lang.String");
    }

    public void testTypeOfListField() {
        assertTypeOfExpression("listField", "java.util.List<java.lang.String>");
    }

    public void ignoreTestTypeOfListItemReturnedByGetFromField() {
        assertTypeOfExpression("listField.get(0)", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByBracketsFromField() {
        assertTypeOfExpression("listField[0]", "java.lang.String");
    }

    public void testTypeOfMapField() {
        assertTypeOfExpression("mapField", "java.util.Map<java.lang.Integer,java.lang.String>");
    }

    public void ignoredTestTypeOfMapValueReturnedByGetFromField() {
        assertTypeOfExpression("mapField.get(0)", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByBracketsFromField() {
        assertTypeOfExpression("mapField[0]", "java.lang.String");
    }

    public void testTypeOfUserTypeField() {
        assertTypeOfExpression("userTypeField", "com.test.ResolvingTypeOfExpression.Nested");
    }

    public void testTypeOfNestedFieldOfUserTypeField() {
        assertTypeOfExpression("userTypeField.nestedStringField", "java.lang.String");
    }

    public void testTypeOfNestedMethodOfUserTypeField() {
        assertTypeOfExpression("userTypeField.nestedStringMethod()", "java.lang.String");
    }



    public void testTypeOfUnresolvedVariable() {
        assertTypeOfExpression("#iAmNotReal", null);
    }

    public void testTypeOfTextDefinedVariable() {
        assertTypeOfExpression("#var", "#var", ConcordionPsiUtils.DYNAMIC.getCanonicalText());
    }

    public void testTypeOfStringInitializedVariable() {
        assertTypeOfExpression("#var = stringMethod()", "#var", "java.lang.String");
    }

    public void testTypeOfArrayLengthAfterVariable() {
        assertTypeOfExpression("#var = arrayMethod()", "#var.length", PsiType.INT.getCanonicalText());
    }

    public void testTypeOfArrayItemReturnedByVariable() {
        assertTypeOfExpression("#var = arrayMethod()", "#var[0]", "java.lang.String");
    }

    public void testTypeOfListVariable() {
        assertTypeOfExpression("#var = listMethod()", "#var", "java.util.List<java.lang.String>");
    }

    public void ignoredTestTypeOfListItemReturnedByGetFromVariable() {
        assertTypeOfExpression("#var = listMethod()", "#var.get(0)", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByBracketsFromVariable() {
        assertTypeOfExpression("#var = listMethod()", "#var[0]", "java.lang.String");
    }

    public void testTypeOfMapVariable() {
        assertTypeOfExpression("#var = mapMethod()", "#var", "java.util.Map<java.lang.Integer,java.lang.String>");
    }

    public void ignoredTestTypeOfMapValueReturnedByGetFromVariable() {
        assertTypeOfExpression("#var = mapMethod()", "#var.get(0)", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByBracketsFromVariable() {
        assertTypeOfExpression("#var = mapMethod()", "#var[0]", "java.lang.String");
    }

    public void testTypeOfUserTypeVariable() {
        assertTypeOfExpression("#var = userTypeMethod()", "#var", "com.test.ResolvingTypeOfExpression.Nested");
    }

    public void testTypeOfNestedFieldOfUserTypeVariable() {
        assertTypeOfExpression("#var = userTypeMethod()", "#var.nestedStringField", "java.lang.String");
    }

    public void testTypeOfNestedMethodOfUserTypeVariable() {
        assertTypeOfExpression("#var = userTypeMethod()", "#var.nestedStringMethod()", "java.lang.String");
    }



    private void assertTypeOfExpression(@NotNull String expression, @Nullable String expectedType) {
        assertTypeOfExpression("", expression, expectedType);
    }

    private void assertTypeOfExpression(@NotNull String variableDeclaration, @NotNull String expression, @Nullable String expectedType) {

        copyTestFixtureToConcordionProject("ResolvingTypeOfExpression.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("ResolvingTypeOfExpression.html", expression, variableDeclaration);

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        ConcordionOgnlExpressionStart expr = getParentOfType(elementUnderCaret(), ConcordionOgnlExpressionStart.class);
        assertThat(expr).isNotNull();
        assertThat(qualifiedTypeWithGenerics(typeOfExpression(expr))).isEqualTo(expectedType);
    }

    @Nullable
    private String qualifiedTypeWithGenerics(@Nullable PsiType type) {
        return type != null ? type.getCanonicalText() : null;
    }
}
