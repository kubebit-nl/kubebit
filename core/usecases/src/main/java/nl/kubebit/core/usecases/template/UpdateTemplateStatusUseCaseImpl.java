package nl.kubebit.core.usecases.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateInvalidStatusException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
@UseCase
class UpdateTemplateStatusUseCaseImpl implements UpdateTemplateStatusUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public UpdateTemplateStatusUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateResponse execute(String templateId, TemplateStatus status) throws TemplateNotFoundException {
        log.info("update template: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(() -> new TemplateNotFoundException(templateId));

        //
        if (template.status() != TemplateStatus.DEPRECATED && template.status() != TemplateStatus.AVAILABLE && template.status() != TemplateStatus.VALIDATED) {
            throw new TemplateInvalidStatusException(template.status());
        }

        //
        if (status != TemplateStatus.DEPRECATED && status != TemplateStatus.AVAILABLE) {
            throw new TemplateInvalidStatusException(status);
        }
        
        // change status     
        var update = template.setStatus(status, null);

        //
        return gateway.updateStatus(update).map(TemplateResponse::new).orElseThrow(TemplateNotUpdatedException::new);
    }
    
}
