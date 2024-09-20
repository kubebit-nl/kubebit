package nl.kubebit.core.usecases.template.admin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
@UseCase
class UpdateTemplateSchemaUseCaseImpl implements UpdateTemplateSchemaUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public UpdateTemplateSchemaUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateItemResponse execute(String templateId, Map<String, Object> schema) throws TemplateNotFoundException {
        log.info("update template schema: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(TemplateNotFoundException::new);
        
        // change schema
        var update = new Template(
            template.id(),
            template.chart(),
            template.version(),
            template.repository(),
            template.type(),
            template.category(),
            template.icon(),
            schema,
            template.baseValues(),
            template.stagingValues(),
            template.productionValues(),
            template.status(),
            template.message(),
            template.values(),
            template.appVersion(),
            template.description(),
            template.keywords(),
            template.namespaceId());    
            
        //
        return gateway.update(update).map(TemplateItemResponse::new).orElseThrow(TemplateNotUpdatedException::new);
    }
    
}
