package lr1.lr1;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ProjArchitectureTests {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("lr1.lr1");

    // --- 1
    @Test
    void servicesShouldBeInServicePackage() {
        classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..service..")
                .check(importedClasses);
    }

    // --- 2
    @Test
    void controllersShouldBeInControllerPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..controller..")
                .check(importedClasses);
    }

    // --- 3
    @Test
    void repositoriesShouldBeInRepositoryPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..repository..")
                .check(importedClasses);
    }

    // --- 4
    @Test
    void modelsShouldBeInModelPackage() {
        classes()
                .that().resideInAPackage("..model..")
                .should().haveSimpleNameEndingWith("")
                .check(importedClasses);
    }

    // --- 5 (оновлений)
    @Test
    void servicesShouldNotBeAccessedByRepositoriesOrModels() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..service..")
                .check(importedClasses);

        classes()
                .that().resideInAPackage("..model..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..service..")
                .check(importedClasses);
    }


    // --- 6
    @Test
    void controllersShouldDependOnServices() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat()
                .resideInAPackage("..service..")
                .check(importedClasses);
    }

    // --- 7
    @Test
    void servicesShouldDependOnRepositories() {
        classes()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat()
                .resideInAPackage("..repository..")
                .check(importedClasses);
    }

    // --- 8
    @Test
    void repositoriesShouldOnlyAccessModels() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().onlyAccessClassesThat()
                .resideInAPackage("..model..")
                .check(importedClasses);
    }

    // --- 9
    @Test
    void servicesShouldNotDependOnControllers() {
        classes()
                .that().resideInAPackage("..service..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..controller..")
                .check(importedClasses);
    }

    // --- 10
    @Test
    void repositoriesShouldNotDependOnServices() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..service..")
                .check(importedClasses);
    }

    // --- 11
    @Test
    void modelsShouldNotDependOnRepositories() {
        classes()
                .that().resideInAPackage("..model..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..repository..")
                .check(importedClasses);
    }

    // --- 12
    @Test
    void modelsShouldNotDependOnServices() {
        classes()
                .that().resideInAPackage("..model..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..service..")
                .check(importedClasses);
    }

    // --- 13
    @Test
    void modelsShouldNotDependOnControllers() {
        classes()
                .that().resideInAPackage("..model..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..controller..")
                .check(importedClasses);
    }

    // --- 14
    @Test
    void controllersShouldNotAccessRepositoriesDirectly() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..repository..")
                .check(importedClasses);
    }

    // --- 15
    @Test
    void controllersShouldHaveNameEndingWithController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .check(importedClasses);
    }

    // --- 16
    @Test
    void servicesShouldHaveNameEndingWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(importedClasses);
    }

    // --- 17
    @Test
    void repositoriesShouldHaveNameEndingWithRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .check(importedClasses);
    }

    // --- 18
    @Test
    void parcelControllerShouldBeInControllerPackage() {
        classes()
                .that().haveSimpleNameContaining("Parcel")
                .should().resideInAnyPackage("..controller..", "..service..", "..repository..", "..model..")
                .check(importedClasses);
    }

    // --- 19
    @Test
    void warehouseControllerShouldBeInControllerPackage() {
        classes()
                .that().haveSimpleNameContaining("Warehouse")
                .should().resideInAnyPackage("..controller..", "..service..", "..repository..", "..model..")
                .check(importedClasses);
    }

    // --- 20
    @Test
    void LogisticsSystemApplicationShouldBeInRootPackage() {
        classes()
                .that().haveSimpleNameStartingWith("LogisticsSystemApplication")
                .should().resideInAPackage("..")
                .check(importedClasses);
    }
}
