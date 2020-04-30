package vdehorta;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class NowInstantTest {

    /**
     * Check that Instant.now is never used by controllers, services or repositories.
     * The ClockService.now method should be used instead, allowing testing instants easily.
     */
    @Test
    void instantNowMethodShouldNotBeUsed() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("vdehorta");

        noClasses()
            .that()
                .resideInAnyPackage("..service..")
            .or()
                .resideInAnyPackage("..repository..")
            .or()
                .resideInAnyPackage("..rest..")
            .should().callMethod(Instant.class, "now")
        .because("ClockService.now() should be used instead of Instant.now()!")
        .check(importedClasses);
    }
}
