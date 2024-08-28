package nl.kubebit.core.usecases.template;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
@Usecase
class UpdateTemplateOverlayUsecaseImpl implements UpdateTemplateOverlayUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     * 
     * @param gateway
     */
    public UpdateTemplateOverlayUsecaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateResponse execute(String templateId, Map<String, Object> overlay) throws TemplateNotFoundException {
        log.info("update template overlay: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(() -> new TemplateNotFoundException(templateId));
        
        // change overlay    
        // change form    
        var update = new Template(
            template.id(),
            template.chart(),
            template.version(),
            template.repository(),
            template.type(),
            template.category(),
            template.icon(),
            template.formSchema(),
            overlay,
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
