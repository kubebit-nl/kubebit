package nl.kubebit.core.usecases.common.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaParameter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import nl.kubebit.core.usecases.common.annotation.UseCase;


/**
 *
 */
public class UseCaseArchTest {
    // --------------------------------------------------------------------------------------------

    //
    private final String packages = "nl.kubebit.core.UseCases";

    /**
     *
     */
    @Test
    void allUseCasesShouldBeAnInterface() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(packages);

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("UseCase")
                .should().beInterfaces();

        rule.check(importedClasses);
    }

    /**
     *
     */
    @Test
    void interfacesEndingWithUseCaseShouldHaveOnlyOneMethodNamedExecute() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(packages);


        ArchCondition<JavaClass> haveOnlyOneMethodNamedExecute = new ArchCondition<>("have only one method named execute") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                long executeMethodCount = javaClass.getMethods().stream()
                        .filter(method -> method.getName().equals("execute"))
                        .count();
                boolean hasOnlyOneExecuteMethod = executeMethodCount == 1 && javaClass.getMethods().size() == 1;
                if (!hasOnlyOneExecuteMethod) {
                    String message = String.format("Class %s does not have exactly one method named execute", javaClass.getName());
                    events.add(SimpleConditionEvent.violated(javaClass, message));
                }
            }
        };

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("UseCase")
                .and()
                .doNotHaveSimpleName("UseCase")
                .should(haveOnlyOneMethodNamedExecute);

        rule.check(importedClasses);
    }

    /**
     *
     */
    @Test
    void executeMethodsInClassesEndingWithUseCaseShouldBeAnnotatedWithJakartaConstraints() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(packages);

        //
        ArchCondition<JavaMethod> beAnnotatedWithJakartaConstraints = new ArchCondition<>("be annotated with Jakarta constraints") {

            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                for (JavaParameter parameter : method.getParameters()) {
                    boolean hasConstraint = parameter.getAnnotations().stream()
                            .anyMatch(annotation -> annotation.getRawType().isAssignableTo(Valid.class) ||
                                    annotation.getRawType().isAssignableTo(NotNull.class) ||
                                    annotation.getRawType().isAssignableTo(NotBlank.class) ||
                                    annotation.getRawType().isAssignableTo(Pattern.class) ||
                                    annotation.getRawType().isAssignableTo(Size.class));
                    if (!hasConstraint) {
                        String message = String.format("Parameter %s in method %s of class %s is not annotated with Jakarta constraints",
                                parameter.getIndex(), method.getName(), method.getOwner().getName());
                        events.add(SimpleConditionEvent.violated(parameter, message));
                    }
                }
            }
        };

        //
        ArchRule rule = ArchRuleDefinition.methods()
                .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("UseCase")
                .and().haveName("execute")
                .should(beAnnotatedWithJakartaConstraints);

        //
        rule.check(importedClasses);
    }

    // ------------------------------------------------

    /**
     *
     */
    @Test
    void allClassesThatAreAnnotatedWithUseCaseMustEndingWith() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(packages);

        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAnnotatedWith(UseCase.class)
                .should().haveSimpleNameEndingWith("UseCaseImpl");

        rule.check(importedClasses);
    }

    /**
     *
     */
    @Test
    void allUseCaseImplementationsShouldBeAnnotatedWithUseCase() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(packages);

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("UseCaseImpl")
                .should().beAnnotatedWith(UseCase.class);

        rule.check(importedClasses);
    }

    /**
     *
     */
    @Test
    void allUseCaseImplementationsMustNotBePublic() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(packages);

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("UseCaseImpl")
                .should().notBePublic();

        rule.check(importedClasses);
    }




}
