package nl.kubebit.core.usecases.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;
import nl.kubebit.core.usecases.template.thread.TemplateValidatingThread;

/**
 * 
 */
@Usecase
class ValidateTemplateUsecaseImpl implements ValidateTemplateUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     * 
     * @param gateway
     */
    public ValidateTemplateUsecaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateResponse execute(String templateId) throws TemplateNotFoundException {
        log.info("validate template: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(() -> new TemplateNotFoundException(templateId));
        
        // change status to validating        
        var result = gateway.updateStatus(template.setStatus(TemplateStatus.VALIDATING, null))
            .orElseThrow(() -> new TemplateNotUpdatedException());

        // start thread
        new TemplateValidatingThread(gateway, result).start();

        //
        return new TemplateResponse(result);
    }

}
