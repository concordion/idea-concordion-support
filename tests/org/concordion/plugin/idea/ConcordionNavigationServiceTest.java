package org.concordion.plugin.idea;

public class ConcordionNavigationServiceTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/navigation";
    }


    public void testSecAndFixtureHaveSameName() {

        assertNavigateToEachOther(
                copyTestFixtureToConcordionProject("Spec1.java"),
                copySpecToConcordionProject("Spec1.html")
        );
    }

    public void testFixtureMayHaveOptionalTestSuffix() {

        assertNavigateToEachOther(
                copyTestFixtureToConcordionProject("Spec2Test.java"),
                copySpecToConcordionProject("Spec2.html")
        );
    }

    public void testSpecShouldNotHaveTestSuffix() {

        assertDoNotNavigate(
                copyTestFixtureToConcordionProject("Spec3Test.java"),
                copySpecToConcordionProject("Spec3Test.html")
        );
    }

    public void testFixtureMayHaveOptionalFixtureSuffix() {

        assertNavigateToEachOther(
                copyTestFixtureToConcordionProject("Spec4Fixture.java"),
                copySpecToConcordionProject("Spec4.html")
        );
    }

    public void testSpecShouldNotHaveFixtureSuffix() {

        assertDoNotNavigate(
                copyTestFixtureToConcordionProject("Spec5Fixture.java"),
                copySpecToConcordionProject("Spec5Fixture.html")
        );
    }

    public void testDoesNotNavigateForNonConcordionPairs() {

        assertDoNotNavigate(
                copyTestFixtureToConcordionProject("NoConcordion.java"),
                copySpecToConcordionProject("NoConcordion.html")
        );
    }

    public void testNavigateIfOnlyNamespacePresent() {

        assertNavigateToEachOther(
                copyTestFixtureToConcordionProject("NoRunnerAnnotation.java"),
                copySpecToConcordionProject("NoRunnerAnnotation.html")
        );
    }

    public void testNavigateIfOnlyRunnerPresent() {

        assertNavigateToEachOther(
                copyTestFixtureToConcordionProject("NoNamespace.java"),
                copySpecToConcordionProject("NoNamespace.html")
        );
    }

    public void testNavigatesWithXhtmlSpec() {

        assertNavigateToEachOther(
                copyTestFixtureToConcordionProject("Spec.java"),
                copySpecToConcordionProject("Spec.xhtml")
        );
    }
}
