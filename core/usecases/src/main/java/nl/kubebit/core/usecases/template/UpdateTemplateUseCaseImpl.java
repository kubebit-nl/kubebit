package nl.kubebit.core.usecases.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
@UseCase
class UpdateTemplateUseCaseImpl implements UpdateTemplateUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public UpdateTemplateUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateResponse execute(String templateId, TemplateUpdateRequest request) throws TemplateNotFoundException {
        log.info("update template: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(() -> new TemplateNotFoundException(templateId));
        
        // change settings     
        var update = new Template(
            template.id(),
            template.chart(),
            template.version(),
            template.repository(),
            request.type(),
            request.category(),
            request.icon(),
            template.formSchema(),
            template.overlayValues(),
            template.status(),
            template.message(),
            template.chartSchema(),
            template.chartValues(),
            template.appVersion(),
            template.description(),
            template.keywords(),
            template.namespaceId());

        //
        return gateway.update(update).map(TemplateResponse::new).orElseThrow(TemplateNotUpdatedException::new);
    }
    
}
