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

    public void testTypeOfListItemReturnedByGetFromMethod() {
        assertTypeOfExpression("listMethod().get(0)", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByBracketsFromMethod() {
        assertTypeOfExpression("listMethod()[0]", "java.lang.String");
    }

    public void testTypeOfNonGenericListItemReturnedByGetFromMethod() {
        assertTypeOfExpression("nonGenericListMethod().get(0)", "java.lang.Object");
    }

    public void testTypeOfNonGenericListItemReturnedByBracketsFromMethod() {
        assertTypeOfExpression("nonGenericListMethod()[0]", "java.lang.Object");
    }

    public void testTypeOfMapMethod() {
        assertTypeOfExpression("mapMethod()", "java.util.Map<java.lang.Character,java.lang.String>");
    }

    public void testTypeOfMapValueReturnedByGetFromMethod() {
        assertTypeOfExpression("mapMethod().get('a')", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByBracketsFromMethod() {
        assertTypeOfExpression("mapMethod()['a']", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByFieldFromMethod() {
        assertTypeOfExpression("mapMethod().a", "java.lang.String");
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

    public void testTypeOfListItemReturnedByGetFromField() {
        assertTypeOfExpression("listField.get(0)", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByBracketsFromField() {
        assertTypeOfExpression("listField[0]", "java.lang.String");
    }

    public void testTypeOfNonGenericListItemReturnedByGetFromField() {
        assertTypeOfExpression("nonGenericListField.get(0)", "java.lang.Object");
    }

    public void testTypeOfNonGenericListItemReturnedByBracketsFromField() {
        assertTypeOfExpression("nonGenericListField[0]", "java.lang.Object");
    }

    public void testTypeOfMapField() {
        assertTypeOfExpression("mapField", "java.util.Map<java.lang.Character,java.lang.String>");
    }

    public void testTypeOfMapValueReturnedByGetFromField() {
        assertTypeOfExpression("mapField.get('a')", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByBracketsFromField() {
        assertTypeOfExpression("mapField['a']", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByFieldFromField() {
        assertTypeOfExpression("mapField.a", "java.lang.String");
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



    public void testTypeOfUnresolvedProperty() {
        assertTypeOfExpression("iAmNotReal", null);
    }

    public void testTypeOfStringProperty() {
        assertTypeOfExpression("stringProperty", "java.lang.String");
    }

    public void testTypeOfArrayLengthAfterProperty() {
        assertTypeOfExpression("arrayProperty.length", PsiType.INT.getCanonicalText());
    }

    public void testTypeOfArrayItemReturnedByProperty() {
        assertTypeOfExpression("arrayProperty[0]", "java.lang.String");
    }

    public void testTypeOfListProperty() {
        assertTypeOfExpression("listProperty", "java.util.List<java.lang.String>");
    }

    public void testTypeOfListItemReturnedByGetFromProperty() {
        assertTypeOfExpression("listProperty.get(0)", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByBracketsFromProperty() {
        assertTypeOfExpression("listProperty[0]", "java.lang.String");
    }

    public void testTypeOfNonGenericListItemReturnedByGetFromProperty() {
        assertTypeOfExpression("nonGenericListProperty.get(0)", "java.lang.Object");
    }

    public void testTypeOfNonGenericListItemReturnedByBracketsFromProperty() {
        assertTypeOfExpression("nonGenericListProperty[0]", "java.lang.Object");
    }

    public void testTypeOfMapProperty() {
        assertTypeOfExpression("mapProperty", "java.util.Map<java.lang.Character,java.lang.String>");
    }

    public void testTypeOfMapValueReturnedByGetFromProperty() {
        assertTypeOfExpression("mapProperty.get('a')", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByBracketsFromProperty() {
        assertTypeOfExpression("mapProperty['a']", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByFieldFromProperty() {
        assertTypeOfExpression("mapProperty.a", "java.lang.String");
    }

    public void testTypeOfUserTypeProperty() {
        assertTypeOfExpression("userTypeProperty", "com.test.ResolvingTypeOfExpression.Nested");
    }

    public void testTypeOfNestedFieldOfUserTypeProperty() {
        assertTypeOfExpression("userTypeProperty.nestedStringField", "java.lang.String");
    }

    public void testTypeOfNestedMethodOfUserTypeProperty() {
        assertTypeOfExpression("userTypeProperty.nestedStringMethod()", "java.lang.String");
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

    public void testTypeOfListItemReturnedByGetFromVariable() {
        assertTypeOfExpression("#var = listMethod()", "#var.get(0)", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByBracketsFromVariable() {
        assertTypeOfExpression("#var = listMethod()", "#var[0]", "java.lang.String");
    }

    public void testTypeOfNonGenericListItemReturnedByGetFromVariable() {
        assertTypeOfExpression("#var = nonGenericListMethod()", "#var.get(0)", "java.lang.Object");
    }

    public void testTypeOfNonGenericListItemReturnedByBracketsFromVariable() {
        assertTypeOfExpression("#var = nonGenericListMethod()", "#var[0]", "java.lang.Object");
    }

    public void testTypeOfMapVariable() {
        assertTypeOfExpression("#var = mapMethod()", "#var", "java.util.Map<java.lang.Character,java.lang.String>");
    }

    public void testTypeOfMapValueReturnedByGetFromVariable() {
        assertTypeOfExpression("#var = mapMethod()", "#var.get('a')", "java.lang.String");
    }

    public void testTypeOfMapValueReturnedByBracketsFromVariable() {
        assertTypeOfExpression("#var = mapMethod()", "#var['a']", "java.lang.String");
    }

    public void testTypeOfListItemReturnedByFieldFromVariable() {
        assertTypeOfExpression("#var = mapMethod()", "#var.a", "java.lang.String");
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
