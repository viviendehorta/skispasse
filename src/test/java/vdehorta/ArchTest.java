package vdehorta;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("vdehorta");

        noClasses()
            .that()
                .resideInAnyPackage("..service..")
            .or()
                .resideInAnyPackage("..repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..vdehorta.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }

    /**
     * Check that LocalDateTime.now() is never used by controllers, services or repositories.
     * The ClockService.now method should be used instead, allowing testing instants easily.
     */
    @Test
    void nowMethodShouldNeverBeCalledInServicesNorRepositoriesNorRestResources() {
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
            .should().callMethod(LocalDateTime.class, "now")
            .because("ClockService.now() should be used instead of Instant.now()!")
            .check(importedClasses);
    }
}
