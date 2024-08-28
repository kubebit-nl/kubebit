package nl.kubebit.core.usecases.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.template.dto.TemplateFullResponse;

/**
 * 
 */
@Usecase
class GetTemplateUsecaseImpl implements GetTemplateUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public GetTemplateUsecaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateFullResponse execute(String templateId) throws TemplateNotFoundException {
        log.info("get template: {}", templateId);
        return gateway.findById(templateId).map(TemplateFullResponse::new).orElseThrow(() -> new TemplateNotFoundException(templateId));
    }

}
