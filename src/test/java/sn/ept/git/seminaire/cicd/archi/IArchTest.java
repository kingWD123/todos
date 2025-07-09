package sn.ept.git.seminaire.cicd.archi;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.lang.syntax.elements.ClassesThat;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IArchTest {

    public static final String BASE = "sn.ept.git.seminaire.cicd";
    public static final String SERVICES = BASE.concat(".services..");
    public static final String REPOSITORY = BASE.concat(".repositories..");
    public static final String ENTITIES = BASE.concat(".entities..");
    public static final String COMPONENTS = BASE.concat(".component..");
    public static final String RESOURCE = BASE.concat(".resources..");
    public static final String MAPPER = BASE.concat(".mappers..");
    private static  ClassesThat<GivenClassesConjunction> classesThat;
    private static  ClassesThat<GivenClassesConjunction> noClassThat;
    private static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .importPackages(BASE);
        classesThat = ArchRuleDefinition.classes().that();
        noClassThat = ArchRuleDefinition.noClasses().that();
    }


    @Test
    void servicesComponentsAndRepositoriesShouldNotDependOnWebLayer() {
        noClassThat
                .resideInAnyPackage(SERVICES )
                .or()
                .resideInAnyPackage(REPOSITORY)
                .or()
                .resideInAnyPackage(COMPONENTS)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(RESOURCE)
                .because("Services, components and repositories should not depend on web layer")
                .check(importedClasses);
    }

    @Test
    void resourcesShouldNotDependOnRepositoryLayer() {
        noClassThat
                .resideInAnyPackage(RESOURCE)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(REPOSITORY)
                .because("Resources should not depend on Repository layer")
                .check(importedClasses);
    }

    @Test
    void resourcesShouldNotUseConcreteClassesOnServiceLayer() {
        noClassThat
                .resideInAPackage(RESOURCE)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage()
                .because("Resources should use Interface in  Service layer instead of concrete classes")
                .check(importedClasses);
    }


    @Test
    void servicesShouldNotDependOnServiceLayer() {
        noClassThat
                .resideInAnyPackage(SERVICES)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(SERVICES)
                .because("Services should not depend on Service layer")
                .check(importedClasses);
    }




    @Test
    void resourcesNamesShouldEndWithResource() {
        classesThat
                .resideInAPackage(RESOURCE)
                .should()
                .haveSimpleNameEndingWith("Resource")
                .because("Resource name should end with Resource")
                .check(importedClasses);
    }

    @Test
    void onlyRepositoriesShouldBeAnnotatedWithRepository() {
        classesThat
                .areAnnotatedWith(org.springframework.stereotype.Repository.class)
                .should()
                .resideInAnyPackage(REPOSITORY)
                .because("Only repositories should be annotated with @Repository")
                .check(importedClasses);
    }


    @Test
    void onlyServicesShouldBeAnnotatedWithService() {
        classesThat
                .areAnnotatedWith(org.springframework.stereotype.Service.class)
                .should()
                .resideInAnyPackage(SERVICES)
                .because("Only services should be annotated with @Service")
                .check(importedClasses);
    }

    @Test
    void onlyResourcesShouldBeAnnotatedWithRestController() {
        classesThat
                .areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                .should()
                .resideInAnyPackage(RESOURCE)
                .because("Only resources should be annotated with @RestController")
                .check(importedClasses);
    }

    @Test
    void onlyDomaineClassesShouldBeAnnotatedWithEntity() {
        classesThat
                .areAnnotatedWith(jakarta.persistence.Entity.class)
                .should()
                .resideInAnyPackage(ENTITIES)
                .because("Only entities should be annotated with @Entity")
                .check(importedClasses);
    }

    @Test
    void onlyDomaineClassesShouldBeAnnotatedWithTable() {
        classesThat
                .areAnnotatedWith(jakarta.persistence.Table.class)
                .should()
                .resideInAnyPackage(ENTITIES)
                .because("Only entities should be annotated with @Table")
                .check(importedClasses);
    }

    @Test
    void onlyMapperClassesShouldBeAnnotatedWithMapper() {
        classesThat
                .areAnnotatedWith(org.mapstruct.Mapper.class)
                .should()
                .resideInAnyPackage(MAPPER)
                .because("Only mappers should reside in package mappers")
                .check(importedClasses);
    }
    @Test
    void noClassOtherThanShouldBeAnnotatedWithMapper() {
        noClassThat
                .resideOutsideOfPackage(MAPPER)
                .should()
                .beAnnotatedWith(org.mapstruct.Mapper.class)
                .because("Only mappers should be annotated with @org.mapstruct.Mapper")
                .check(importedClasses);
    }


}
