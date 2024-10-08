package nl.kubebit.core.usecases.template.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
@UseCase
class GetTemplateUseCaseImpl implements GetTemplateUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public GetTemplateUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateResponse execute(String templateId) throws TemplateNotFoundException {
        log.info("get template: {}", templateId);
        return gateway.findById(templateId).map(TemplateResponse::new).orElseThrow(TemplateNotFoundException::new);
    }

}
