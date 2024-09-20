package nl.kubebit.core.usecases.template.admin;

import nl.kubebit.core.usecases.template.chore.TemplateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
@UseCase
class ValidateTemplateUseCaseImpl implements ValidateTemplateUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    private final TemplateValidator validator;

    /**
     *
     */
    public ValidateTemplateUseCaseImpl(TemplateGateway gateway, TemplateValidator validator) {
        this.gateway = gateway;
        this.validator = validator;
    }

    /**
     * 
     */
    @Override
    public TemplateItemResponse execute(String templateId) throws TemplateNotFoundException {
        log.info("validate template: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(TemplateNotFoundException::new);
        
        // change status to validating        
        var result = gateway.updateStatus(template.setStatus(TemplateStatus.VALIDATING, null))
            .orElseThrow(TemplateNotUpdatedException::new);

        // start validation async
        validator.execute(result);

        //
        return new TemplateItemResponse(result);
    }

}
